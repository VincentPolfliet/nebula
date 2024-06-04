package dev.vinpol.nebula.dragonship.automation.command;

import dev.vinpol.nebula.dragonship.automation.ActivityState;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ShipBehaviourScheduler;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.*;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Function;

public class ShipCommander {

    private static final int DEFAULT_MAX_RESCHEDULE_COUNT = 12;

    private final Logger logger = LoggerFactory.getLogger(ShipCommander.class);

    private final Map<String, ShipBehaviourResult> lastResults = new ConcurrentHashMap<>();
    private final Map<String, Integer> lastResultIsSameCount = new ConcurrentHashMap<>();

    private final FleetApi fleetApi;
    private final ShipBehaviourScheduler scheduler;
    private final int maxRescheduleCount;

    public ShipCommander(ShipBehaviourScheduler scheduler, FleetApi fleetApi) {
        this(scheduler, fleetApi, defaultConfig());
    }

    private static Config defaultConfig() {
        return new Config(
            DEFAULT_MAX_RESCHEDULE_COUNT
        );
    }

    public ShipCommander(ShipBehaviourScheduler scheduler, FleetApi fleetApi, Config config) {
        this.fleetApi = fleetApi;
        this.scheduler = Objects.requireNonNull(scheduler);
        this.maxRescheduleCount = Math.abs(config.maxRescheduleAmount());
    }

    public CompletionStage<?> command(Ship ship) {
        Objects.requireNonNull(ship);

        String shipSymbol = ship.getSymbol();
        logger.debug("Ship: {}", shipSymbol);

        if (scheduler.isTickScheduled(ship)) {
            logger.info("Ship '{}' already has a tick scheduled, skipping it", shipSymbol);
            return CompletableFuture.completedStage(null);
        }

        return commandWithFunction(ship, _ -> ShipBehaviour.finished());
    }

    public CompletionStage<?> command(Ship ship, ShipBehaviour override) {
        return commandWithFunction(ship, _ -> override);
    }

    public CompletionStage<?> command(Ship ship, Function<Ship, ShipBehaviour> nextBehaviour) {
        return commandWithFunction(ship, nextBehaviour);
    }


    public CompletableFuture<?> commandWithFunction(Ship ship, Function<Ship, ShipBehaviour> nextBehaviour) {
        CompletableFuture<ShipBehaviourResult> future = scheduler.scheduleTick(ship.getSymbol(), shipSymbol -> fleetApi.getMyShip(shipSymbol).getData(), nextBehaviour);
        return handlerFuture(ship, future, nextBehaviour);
    }

    private CompletableFuture<?> internalScheduleAtAndHandle(Ship ship, OffsetDateTime at) {
        CompletableFuture<ShipBehaviourResult> future = scheduler.scheduleTickAt(ship.getSymbol(), shipSymbol -> fleetApi.getMyShip(shipSymbol).getData(), _ -> ShipBehaviour.finished(), at);
        return handlerFuture(ship, future, _ -> ShipBehaviour.finished());
    }

    private CompletableFuture<?> handlerFuture(Ship ship, CompletableFuture<ShipBehaviourResult> future, Function<Ship, ShipBehaviour> shipBehaviourResolver) {
        return future
            .thenCompose(result -> handleResult(ship, result, shipBehaviourResolver));
    }

    private CompletionStage<?> handleResult(Ship ship, ShipBehaviourResult result, Function<Ship, ShipBehaviour> shipBehaviourResolver) {
        String shipSymbol = ship.getSymbol();
        ShipBehaviourResult lastResult = lastResults.get(shipSymbol);

        if (lastResult != null && Objects.equals(lastResult, result)) {
            int currentSameResultCount = lastResultIsSameCount.merge(shipSymbol, 1, Integer::sum);

            if (currentSameResultCount > maxRescheduleCount) {
                // Cleanup resources to prevent counting against previous scheduling
                lastResults.remove(shipSymbol);
                lastResultIsSameCount.remove(shipSymbol);
                return CompletableFuture.failedStage(new MaxRescheduleCountExceededException(
                    "%s has reached max reschedule count, the behaviour didn't send a Done event within %d steps".formatted(shipSymbol, maxRescheduleCount), shipSymbol, maxRescheduleCount));
            }
        }

        lastResults.put(shipSymbol, result);

        switch (result) {
            // success means that the tick has been completed without failure, and there are still steps to execute
            case Success success -> {
                // reschedule this bad boi
                return commandWithFunction(ship, shipBehaviourResolver);
            }
            case WaitUntil waitUntil -> {
                // reschedule it at a given timestamp
                return internalScheduleAtAndHandle(ship, waitUntil.timestamp());
            }
            case Done done -> {
                lastResults.remove(shipSymbol);
                lastResultIsSameCount.remove(shipSymbol);
                // nothing to do, the behaviour considers itself done
                return CompletableFuture.completedStage(null);
            }
            case Failed failed -> {
                logger.error("Something went wrong with running the behaviour: {}", failed.message());
                return CompletableFuture.failedStage(new RuntimeException(failed.message()));
            }

            default -> throw new IllegalArgumentException();
        }
    }

    public Optional<ShipBehaviour> getCurrentBehaviour(Ship ship) {
        if (!scheduler.isTickScheduled(ship)) {
            return Optional.empty();
        }

        return scheduler.getCurrentBehaviour(ship.getSymbol());
    }

    public void cancel(Ship ship) {
        Objects.requireNonNull(ship);

        scheduler.cancelTick(ship.getSymbol());
    }

    public record Config(int maxRescheduleAmount) {

    }
}
