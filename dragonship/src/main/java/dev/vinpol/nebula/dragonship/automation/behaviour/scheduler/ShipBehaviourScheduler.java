package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static dev.vinpol.nebula.dragonship.utils.RuntimeExceptionUtils.rethrowIfPossible;

public class ShipBehaviourScheduler {

    private final Logger logger = LoggerFactory.getLogger(ShipBehaviourScheduler.class);

    private final Map<String, ShipBehaviourTask> activeBehaviours = new ConcurrentHashMap<>();

    private final ScheduledExecutor timer;
    private final Executor worker;

    public ShipBehaviourScheduler(ScheduledExecutor timer, Executor worker) {
        this.timer = timer;
        this.worker = worker;
    }

    public CompletableFuture<ShipBehaviorResult> scheduleTick(String shipSymbol, Function<String, Ship> shipSupplier, Function<Ship, ShipBehaviour> behaviourResolver) {
        Objects.requireNonNull(shipSymbol);
        Objects.requireNonNull(shipSupplier);
        Objects.requireNonNull(behaviourResolver);

        return internalScheduleTick(new ActualShipBehaviourTask(
            shipSymbol,
            shipSupplier,
            behaviourResolver
        ));
    }

    public CompletableFuture<ShipBehaviorResult> scheduleTickAt(String shipSymbol,
                                                                Function<String, Ship> shipResolver,
                                                                Function<Ship, ShipBehaviour> behaviourResolver, OffsetDateTime at) {
        Objects.requireNonNull(shipSymbol);
        Objects.requireNonNull(shipResolver);
        Objects.requireNonNull(behaviourResolver);
        Objects.requireNonNull(at);

        logger.debug("adding {} to futuresScheduled", shipSymbol);
        logger.debug("scheduling {}", shipSymbol);

        ActualShipBehaviourTask task = new ActualShipBehaviourTask(
            shipSymbol,
            shipResolver,
            behaviourResolver
        );

        CompletableFuture<ShipBehaviorResult> scheduledFuture = timer.scheduleAt(() -> internalScheduleTick(task), at)
            .thenCompose(Function.identity());

        activeBehaviours.put(shipSymbol, new ScheduledFutureTask(shipSymbol, at, scheduledFuture));

        return scheduledFuture
            .handle(shipBehaviourHandler(task, shipSymbol));
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

        return activeBehaviours.containsKey(shipSymbol);
    }

    public boolean cancelTick(String shipSymbol) {
        if (!isTickScheduled(shipSymbol)) {
            return false;
        }

        ShipBehaviourTask task = activeBehaviours.remove(shipSymbol);

        if (task != null) {
            task.future().cancel(true);
        }

        return true;
    }

    private CompletableFuture<ShipBehaviorResult> internalScheduleTick(ShipBehaviourTask task) {
        String shipSymbol = task.shipSymbol();

        logger.debug("adding {} to active", shipSymbol);
        activeBehaviours.put(shipSymbol, task);

        return CompletableFuture.runAsync(task, worker)
            .thenCompose(_ -> task.future())
            .handle(shipBehaviourHandler(task, shipSymbol));
    }

    private @NotNull BiFunction<ShipBehaviorResult, Throwable, ShipBehaviorResult> shipBehaviourHandler(ShipBehaviourTask task, String shipSymbol) {
        return (result, throwable) -> {
            activeBehaviours.remove(shipSymbol);

            if (throwable != null) {
                if (throwable instanceof CompletionException completionException) {
                    if (completionException.getCause() instanceof CancellationException cancellationException) {
                        // reset the future so that it supports isCancelled;
                        task.future().completeExceptionally(cancellationException);
                        return result;
                    }
                }

                if (throwable instanceof CancellationException cancellationException) {
                    // reset the future so that it supports isCancelled;
                    task.future().completeExceptionally(cancellationException);
                    return result;
                }

                rethrowIfPossible(throwable);
            }

            return result;
        };
    }

    public Optional<ShipBehaviour> getCurrentBehaviour(String symbol) {
        return Optional.ofNullable(activeBehaviours.getOrDefault(symbol, null))
            .map(ShipBehaviourTask::behaviour);
    }
}
