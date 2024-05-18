package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
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
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ShipBehaviourScheduler {

    private final Logger logger = LoggerFactory.getLogger(ShipBehaviourScheduler.class);

    private final Map<String, ShipBehaviourTask> activeBehaviours = new ConcurrentHashMap<>();
    private final Set<String> futuresScheduled = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final FleetApi fleetApi;
    private final ScheduledExecutor timer;
    private final Executor worker;

    public ShipBehaviourScheduler(FleetApi fleetApi, ScheduledExecutor timer, Executor worker) {
        this.fleetApi = fleetApi;
        this.timer = timer;
        this.worker = worker;
    }

    public CompletionStage<ShipBehaviourResult> scheduleTickAt(String shipSymbol, Function<Ship, ShipBehaviour> behaviourResolver, OffsetDateTime at) {
        Objects.requireNonNull(shipSymbol);
        Objects.requireNonNull(behaviourResolver);
        Objects.requireNonNull(at);

        logger.debug("adding {} to futuresScheduled", shipSymbol);
        futuresScheduled.add(shipSymbol);

        return timer.scheduleAtAsCompletableFuture(() -> {
            // do nothing but schedule
        }, at).thenCompose(unused -> {
            logger.debug("scheduling {}", shipSymbol);

            return internalScheduleTick(new ShipBehaviourTask(
                shipSymbol,
                fleetApi,
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

        return activeBehaviours.containsKey(shipSymbol) || futuresScheduled.contains(shipSymbol);
    }

    public CompletionStage<ShipBehaviourResult> scheduleTick(String shipSymbol, Function<Ship, ShipBehaviour> behaviourResolver) {
        Objects.requireNonNull(shipSymbol);
        Objects.requireNonNull(behaviourResolver);

        return internalScheduleTick(new ShipBehaviourTask(
            shipSymbol,
            fleetApi,
            behaviourResolver
        ));
    }

    private CompletionStage<ShipBehaviourResult> internalScheduleTick(ShipBehaviourTask task) {
        String shipSymbol = task.getShipSymbol();

        logger.debug("adding {} to active", shipSymbol);
        activeBehaviours.put(shipSymbol, task);

        // futures get removed here so isTickScheduled doesn't return false when another thread would call that method during this period
        if (futuresScheduled.contains(shipSymbol)) {
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

    static class ShipBehaviourTask implements Supplier<ShipBehaviourResult> {

        private final Logger logger = LoggerFactory.getLogger(ShipBehaviourTask.class);

        private final String shipSymbol;
        private final FleetApi fleetApi;

        private final Function<Ship, ShipBehaviour> shipBehaviourFunction;
        private ShipBehaviour currentBehaviour;
        private Thread runnerThread;

        public ShipBehaviourTask(String shipSymbol, FleetApi fleetApi, Function<Ship, ShipBehaviour> resolver) {
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
                Ship ship = fleetApi.getMyShip(shipSymbol).getData();

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
