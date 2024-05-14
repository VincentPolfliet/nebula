package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithm;
import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ShipBehaviourScheduler;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ScheduledExecutor;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.Done;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipMother;
import dev.vinpol.spacetraders.sdk.models.ShipRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult.done;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShipBehaviourSchedulerTest {

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    ScheduledExecutor timer = ScheduledExecutor.ofScheduledExecutorService(scheduler);
    ExecutorService worker = Executors.newSingleThreadExecutor();

    ShipAlgorithmResolver resolver;
    ShipAlgorithm algorithm;
    ShipBehaviourScheduler sut;

    @BeforeEach
    void setup() {
        algorithm = mock(ShipAlgorithm.class);
        resolver = new ShipAlgorithmResolver(
            Map.of(
                ShipRole.EXCAVATOR,
                algorithm
            )
        );

        sut = new ShipBehaviourScheduler(timer, worker);
    }

    @AfterEach
    void breakdown() {
        scheduler.shutdownNow();
        worker.shutdownNow();
    }

    @Test
    void scheduleTick() {
        Ship ship = ShipMother.excavator();
        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofResult(done()));

        CompletableFuture<ShipBehaviourResult> stage = sut.scheduleTick(ship, resolver).toCompletableFuture();

        await().until(stage::isDone);
        ShipBehaviourResult result = stage.getNow(null);
        assertThat(result).isInstanceOf(Done.class);
        assertThat(sut.isTickScheduled(ship)).isFalse();
    }

    @Test
    void scheduleTickAt() {
        Ship ship = ShipMother.excavator();
        OffsetDateTime timestamp = OffsetDateTime.now();
        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofResult(done()));

        CompletableFuture<ShipBehaviourResult> stage = sut.scheduleTickAt(ship, resolver, timestamp).toCompletableFuture();

        await().until(stage::isDone);
        ShipBehaviourResult result = stage.getNow(null);
        assertThat(result).isInstanceOf(Done.class);
        assertThat(sut.isTickScheduled(ship)).isFalse();
    }

    @Test
    void scheduleTickThrowsExceptionWhenParamsAreNull() {
        assertThatNullPointerException()
            .isThrownBy(() -> {
                sut.scheduleTick(null, new ShipAlgorithmResolver(Collections.emptyMap()));
            });

        assertThatNullPointerException()
            .isThrownBy(() -> {
                sut.scheduleTick(new Ship(), null);
            });
    }

    @Test
    void isTickScheduledReturnsFalseWhenShipIsNull() {
        assertFalse(sut.isTickScheduled(null));
    }

    @Test
    void isTickScheduledReturnsTrueWhenScheduled() {
        Ship ship = ShipMother.excavator();
        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofResult(done()));

        OffsetDateTime timestamp = OffsetDateTime.now();
        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTickAt(ship, resolver, timestamp).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        await().until(future::isDone);
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledThrowsExceptionWhenAlgorithmThrowsException() {
        Ship ship = ShipMother.excavator();
        when(algorithm.decideBehaviour(ship)).thenThrow(RuntimeException.class);

        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTick(ship, resolver).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledThrowsExceptionWhenBehaviourThrowsException() {
        Ship ship = ShipMother.excavator();
        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofSupplier(() -> {
            throw new RuntimeException();
        }));

        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTick(ship, resolver).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledAtThrowsExceptionWhenAlgorithmThrowsException() {
        Ship ship = ShipMother.excavator();
        when(algorithm.decideBehaviour(ship)).thenThrow(RuntimeException.class);

        OffsetDateTime timestamp = OffsetDateTime.now();
        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTickAt(ship, resolver, timestamp).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledAtThrowsExceptionWhenBehaviourThrowsException() {
        Ship ship = ShipMother.excavator();
        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofSupplier(() -> {
            throw new RuntimeException();
        }));

        OffsetDateTime timestamp = OffsetDateTime.now();
        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTickAt(ship, resolver, timestamp).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }
}
