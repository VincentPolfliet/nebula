package dev.vinpol.nebula.dragonship.automation.command;

import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ShipBehaviourScheduler;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.*;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ShipCommander {

    private final Logger logger = LoggerFactory.getLogger(ShipCommander.class);

    private final ShipAlgorithmResolver algorithms;
    private final ShipBehaviourScheduler scheduler;

    public ShipCommander(ShipAlgorithmResolver algorithms, ShipBehaviourScheduler scheduler) {
        this.algorithms = Objects.requireNonNull(algorithms);
        this.scheduler = Objects.requireNonNull(scheduler);
    }

    public CompletionStage<?> command(Ship ship) {
        Objects.requireNonNull(ship);

        String shipSymbol = ship.getSymbol();
        logger.info("Ship: {}", shipSymbol);

        if (scheduler.isTickScheduled(ship)) {
            logger.info("Ship '{}' already has a tick scheduled, skipping it", shipSymbol);
            return CompletableFuture.completedStage(null);
        }

        return internalScheduleAndHandle(ship);
    }

    private CompletionStage<?> internalScheduleAndHandle(Ship ship) {
        CompletionStage<ShipBehaviourResult> future = scheduler.scheduleTick(ship, algorithms);
        return handlerFuture(ship, future);
    }

    private CompletionStage<?> internalAtScheduleAndHandle(Ship ship, OffsetDateTime at) {
        CompletionStage<ShipBehaviourResult> future = scheduler.scheduleTickAt(ship, algorithms, at);
        return handlerFuture(ship, future);
    }

    private CompletionStage<?> handlerFuture(Ship ship, CompletionStage<ShipBehaviourResult> future) {
        return future
                .thenComposeAsync(result -> handleResult(ship, result));
    }

    private CompletionStage<?> handleResult(Ship ship, ShipBehaviourResult result) {
        switch (result) {
            // success means that the tick has been completed without failure, and there are still steps to execute
            case Success success -> {
                // reschedule this bad boi
                return internalScheduleAndHandle(ship);
            }
            case WaitUntil waitUntil -> {
                // reschedule it at a given timestamp
                return internalAtScheduleAndHandle(ship, waitUntil.waitUntil());
            }
            case Done done -> {
                // nothing to do, the behaviour considers itself done
                return CompletableFuture.completedStage(null);
            }
            case Failed failed -> {
                logger.error("Something went wrong with running the behaviour");
                return CompletableFuture.failedStage(new RuntimeException());
            }

            default -> throw new IllegalArgumentException();
        }
    }
}
