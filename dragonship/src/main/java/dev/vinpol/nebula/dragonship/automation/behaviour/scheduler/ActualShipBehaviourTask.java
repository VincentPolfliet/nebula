package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class ActualShipBehaviourTask implements ShipBehaviourTask {
    private final Logger logger = LoggerFactory.getLogger(ActualShipBehaviourTask.class);

    private final String shipSymbol;
    private final Function<String, Ship> shipResolver;
    private final CompletableFuture<ShipBehaviorResult> future = new CompletableFuture<>();

    private final Function<Ship, ShipBehaviour> behaviourResolver;

    private ShipBehaviour currentBehaviour;
    private Thread runnerThread;

    public ActualShipBehaviourTask(String shipSymbol, Function<String, Ship> shipResolver, Function<Ship, ShipBehaviour> behaviourResolver) {
        this.shipResolver = shipResolver;
        this.shipSymbol = shipSymbol;
        this.behaviourResolver = behaviourResolver;
    }

    public String shipSymbol() {
        return shipSymbol;
    }

    public ShipBehaviour behaviour() {
        return currentBehaviour;
    }

    @Override
    public CompletableFuture<ShipBehaviorResult> future() {
        return future;
    }

    public Thread getRunnerThread() {
        return runnerThread;
    }

    @Override
    public void run() {
        runnerThread = Thread.currentThread();

        try {
            if (runnerThread.isInterrupted()) {
                future.complete(ShipBehaviorResult.failure(FailureReason.FAILURE));
                return;
            }

            Ship ship = shipResolver.apply(shipSymbol);

            // TODO: put caching behaviour in a seperate ShipBehaviour wrapper
            if (currentBehaviour == null) {
                currentBehaviour = behaviourResolver.apply(ship);
                logger.debug("currentBehaviour for {}: {}", shipSymbol, currentBehaviour.getName());
            }

            ShipBehaviorResult result = currentBehaviour.update(ship);
            future.complete(result);
        } catch (Exception e) {
            future.completeExceptionally(e);
        } finally {
            runnerThread = null;
        }
    }
}
