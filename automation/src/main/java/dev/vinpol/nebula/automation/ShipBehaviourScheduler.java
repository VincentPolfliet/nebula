package dev.vinpol.nebula.automation;

import dev.vinpol.nebula.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class ShipBehaviourScheduler {

    private final Logger logger = LoggerFactory.getLogger(ShipBehaviourScheduler.class);

    private final Map<String, ShipBehaviourTask> activeBehaviours = new ConcurrentHashMap<>();
    private final Set<String> futuresScheduled = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final ScheduledExecutor timer;
    private final Executor worker;

    public ShipBehaviourScheduler(ScheduledExecutor timer, Executor worker) {
        this.timer = timer;
        this.worker = worker;
    }

    public CompletionStage<ShipBehaviourResult> scheduleTick(Ship ship, ShipAlgorithmResolver resolver) {
        Objects.requireNonNull(ship);
        logger.debug("command: {}", resolver);
        Objects.requireNonNull(resolver);

        ShipRole role = ship.getRegistration().getRole();
        logger.debug("role: {}", role);

        ShipBehaviourTask task = new ShipBehaviourTask(
            ship,
            resolver
        );

        activeBehaviours.put(ship.getSymbol(), task);
        return CompletableFuture.supplyAsync(task, worker)
            .handle((ShipBehaviourResult result, Throwable throwable) -> {
                activeBehaviours.remove(ship.getSymbol());

                if (throwable != null) {
                    throw new RuntimeException(throwable);
                }

                return result;
            });
    }

    public CompletionStage<ShipBehaviourResult> scheduleTickAt(Ship ship, ShipAlgorithmResolver resolver, OffsetDateTime at) {
        futuresScheduled.add(ship.getSymbol());

        return CompletableFuture
            .runAsync(() -> {
                futuresScheduled.remove(ship.getSymbol());
            }, command -> timer.scheduleAt(command, at))
            .thenCompose(unused -> scheduleTick(ship, resolver));
    }

    public boolean isTickScheduled(Ship ship) {
        if (ship == null) {
            return false;
        }

        String shipSymbol = ship.getSymbol();
        return activeBehaviours.containsKey(shipSymbol) || futuresScheduled.contains(shipSymbol);
    }

    static class ShipBehaviourTask implements Supplier<ShipBehaviourResult> {

        private final Ship ship;
        private final ShipAlgorithmResolver resolver;
        private ShipBehaviour currentBehaviour;
        private Thread runnerThread;

        public ShipBehaviourTask(Ship ship, ShipAlgorithmResolver resolver) {
            this.ship = ship;
            this.resolver = resolver;
        }

        public Thread getRunnerThread() {
            return runnerThread;
        }

        @Override
        public ShipBehaviourResult get() {
            runnerThread = Thread.currentThread();

            if (currentBehaviour == null) {
                currentBehaviour = resolver.resolve(ship.getRole()).decideBehaviour(ship);
            }

            try {
                return currentBehaviour.update(ship);
            } finally {
                runnerThread = null;
            }
        }
    }
}
