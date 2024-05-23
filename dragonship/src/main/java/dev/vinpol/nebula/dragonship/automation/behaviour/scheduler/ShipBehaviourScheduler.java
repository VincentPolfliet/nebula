package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ShipBehaviourScheduler {

    private final Logger logger = LoggerFactory.getLogger(ShipBehaviourScheduler.class);

    private final Map<String, ShipBehaviourTask> activeBehaviours = new ConcurrentHashMap<>();
    private final Map<String, OffsetDateTime> futuresScheduled = new ConcurrentHashMap<>();

    private final ScheduledExecutor timer;
    private final ExecutorService worker;

    public ShipBehaviourScheduler(ScheduledExecutor timer, ExecutorService worker) {
        this.timer = timer;
        this.worker = worker;
    }

    public CompletionStage<ShipBehaviourResult> scheduleTick(String shipSymbol, Function<String, Ship> shipSupplier, Function<Ship, ShipBehaviour> behaviourResolver) {
        Objects.requireNonNull(shipSymbol);
        Objects.requireNonNull(shipSupplier);
        Objects.requireNonNull(behaviourResolver);

        return internalScheduleTick(new ShipBehaviourTask(
            shipSymbol,
            shipSupplier,
            behaviourResolver
        ));
    }

    public CompletionStage<ShipBehaviourResult> scheduleTickAt(String shipSymbol,
                                                               Function<String, Ship> shipResolver,
                                                               Function<Ship, ShipBehaviour> behaviourResolver, OffsetDateTime at) {
        Objects.requireNonNull(shipSymbol);
        Objects.requireNonNull(shipResolver);
        Objects.requireNonNull(behaviourResolver);
        Objects.requireNonNull(at);

        logger.debug("adding {} to futuresScheduled", shipSymbol);
        futuresScheduled.put(shipSymbol, at);

        return timer.scheduleAtAsCompletableFuture(() -> {
            // do nothing but schedule
        }, at).thenCompose(_ -> {
            logger.debug("scheduling {}", shipSymbol);

            return internalScheduleTick(new ShipBehaviourTask(
                shipSymbol,
                shipResolver,
                behaviourResolver
            ));
        });
    }

    public boolean isTickScheduled(Ship ship) {
        if (ship == null) {
            return false;
        }

        return isTickScheduled(ship.getSymbol());
    }

    public boolean isTickScheduled(String shipSymbol) {
        if (shipSymbol == null) {
            return false;
        }

        return activeBehaviours.containsKey(shipSymbol) || futuresScheduled.containsKey(shipSymbol);
    }

    private CompletionStage<ShipBehaviourResult> internalScheduleTick(ShipBehaviourTask task) {
        String shipSymbol = task.getShipSymbol();

        logger.debug("adding {} to active", shipSymbol);
        activeBehaviours.put(shipSymbol, task);

        // futures get removed here so isTickScheduled doesn't return false when another thread would call that method during this period
        if (futuresScheduled.containsKey(shipSymbol)) {
            logger.debug("removing {} from futures", shipSymbol);
            futuresScheduled.remove(shipSymbol);
        }

        return CompletableFuture.supplyAsync(task, worker)
            .handle((ShipBehaviourResult result, Throwable throwable) -> {
                activeBehaviours.remove(shipSymbol);

                if (throwable != null) {
                    throw new RuntimeException(throwable);
                }

                return result;
            });
    }

    /**
     * Retrieves the current task for a ship identified by its symbol as an {@link Optional} of {@link ShipBehaviour}.
     * <p>
     * If the ship is scheduled for a future task, the method returns an {@link Optional} containing a {@link ShipBehaviour}
     * that represents waiting until the scheduled time. If the ship has an active behaviour, it returns an
     * {@link Optional} containing the current behaviour. If neither a scheduled task nor an active behaviour is found,
     * it returns an empty {@link Optional}.
     *
     * @param symbol the unique identifier of the ship.
     * @return an {@link Optional} containing the current {@link ShipBehaviour} for the ship, or an empty {@link Optional}
     * if no task is scheduled or active for the given symbol.
     */
    public Optional<ShipBehaviour> getCurrentBehaviour(String symbol) {
        if (futuresScheduled.containsKey(symbol)) {
            OffsetDateTime waitUntil = futuresScheduled.get(symbol);
            return Optional.of(ShipBehaviour.ofResult(new WaitUntil(waitUntil)));
        }

        return Optional.ofNullable(activeBehaviours.getOrDefault(symbol, null))
            .map(ShipBehaviourTask::getCurrentBehaviour);
    }

    static class ShipBehaviourTask implements Supplier<ShipBehaviourResult> {

        private final Logger logger = LoggerFactory.getLogger(ShipBehaviourTask.class);

        private final String shipSymbol;
        private final Function<String, Ship> fleetApi;

        private final Function<Ship, ShipBehaviour> shipBehaviourFunction;
        private ShipBehaviour currentBehaviour;
        private Thread runnerThread;

        public ShipBehaviourTask(String shipSymbol, Function<String, Ship> fleetApi, Function<Ship, ShipBehaviour> resolver) {
            this.fleetApi = fleetApi;
            this.shipSymbol = shipSymbol;
            this.shipBehaviourFunction = resolver;
        }

        public String getShipSymbol() {
            return shipSymbol;
        }

        public ShipBehaviour getCurrentBehaviour() {
            return currentBehaviour;
        }

        public Thread getRunnerThread() {
            return runnerThread;
        }

        @Override
        public ShipBehaviourResult get() {
            runnerThread = Thread.currentThread();

            try {
                Ship ship = fleetApi.apply(shipSymbol);

                if (currentBehaviour == null) {
                    currentBehaviour = shipBehaviourFunction.apply(ship);
                    logger.debug("currentBehaviour for {}: {}", shipSymbol, currentBehaviour.getName());
                }

                return currentBehaviour.update(ship);
            } finally {
                runnerThread = null;
            }
        }
    }
}
