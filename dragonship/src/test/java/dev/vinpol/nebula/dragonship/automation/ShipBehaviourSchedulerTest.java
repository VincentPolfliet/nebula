package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithm;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ShipBehaviourScheduler;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ScheduledExecutor;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.Done;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.support.mockito.AwaitableAnswer;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.GetMyShip200Response;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.MotherShip;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.concurrent.*;
import java.util.function.Function;

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

    FleetApi fleetApi;
    ShipAlgorithm algorithm;
    ShipBehaviourScheduler sut;
    Function<Ship, ShipBehaviour> resolver;

    @BeforeEach
    void setup() {
        fleetApi = mock(FleetApi.class);
        algorithm = mock(ShipAlgorithm.class);
        resolver = (updated) -> algorithm.decideBehaviour(updated);

        sut = new ShipBehaviourScheduler(fleetApi, timer, worker);
    }

    @AfterEach
    void breakdown() throws Exception {
        scheduler.shutdownNow();
        worker.shutdownNow();
        timer.close();
    }

    @Test
    void scheduleTick() {
        Ship ship = MotherShip.excavator();
        String shipSymbol = ship.getSymbol();

        when(fleetApi.getMyShip(shipSymbol)).thenReturn(new GetMyShip200Response().data(ship));
        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofResult(done()));

        CompletableFuture<ShipBehaviourResult> stage = sut.scheduleTick(shipSymbol, resolver).toCompletableFuture();

        await().until(stage::isDone);
        ShipBehaviourResult result = stage.getNow(null);
        assertThat(result).isInstanceOf(Done.class);
        assertThat(sut.isTickScheduled(ship)).isFalse();
    }

    @Test
    void scheduleTickAt() {
        Ship ship = MotherShip.excavator();
        String shipSymbol = ship.getSymbol();

        OffsetDateTime timestamp = OffsetDateTime.now();

        when(fleetApi.getMyShip(shipSymbol)).thenReturn(new GetMyShip200Response().data(ship));
        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofResult(done()));

        CompletableFuture<ShipBehaviourResult> stage = sut.scheduleTickAt(shipSymbol, resolver, timestamp).toCompletableFuture();

        await().until(stage::isDone);
        ShipBehaviourResult result = stage.getNow(null);
        assertThat(result).isInstanceOf(Done.class);
        assertThat(sut.isTickScheduled(ship)).isFalse();
    }

    @Test
    void scheduleTickThrowsExceptionWhenParamsAreNull() {
        assertThatNullPointerException()
            .isThrownBy(() -> {
                sut.scheduleTick(null, ship -> ShipBehaviour.finished());
            });

        assertThatNullPointerException()
            .isThrownBy(() -> {
                sut.scheduleTick("symbol", null);
            });
    }

    @Test
    void isTickScheduledReturnsFalseWhenShipIsNull() {
        assertFalse(sut.isTickScheduled((Ship) null));
    }

    @Test
    void isTickScheduledReturnsTrueWhenScheduledAtNow() {
        Ship ship = MotherShip.excavator();
        String shipSymbol = ship.getSymbol();

        when(fleetApi.getMyShip(shipSymbol)).thenReturn(new GetMyShip200Response().data(ship));
        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofResult(done()));

        OffsetDateTime timestamp = OffsetDateTime.now().plusNanos(300);
        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTickAt(shipSymbol, resolver, timestamp).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        await().until(future::isDone);
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledThrowsExceptionWhenAlgorithmThrowsException() {
        Ship ship = MotherShip.excavator();
        String shipSymbol = ship.getSymbol();

        when(fleetApi.getMyShip(shipSymbol)).thenReturn(new GetMyShip200Response().data(ship));

        // because it's async, the completable future could be completed before the isTickScheduled assert is ran causing it to fail
        // i'm too stupid to know if await() has functionality for this
        AwaitableAnswer<Object> answer = AwaitableAnswer.await(() -> {
            throw new RuntimeException();
        });

        when(algorithm.decideBehaviour(ship)).thenAnswer(answer);

        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTick(shipSymbol, resolver).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        answer.release();
        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledThrowsExceptionWhenBehaviourThrowsException() {
        Ship ship = MotherShip.excavator();
        String shipSymbol = ship.getSymbol();

        when(fleetApi.getMyShip(shipSymbol)).thenReturn(new GetMyShip200Response().data(ship));

        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofSupplier(() -> {
            throw new RuntimeException();
        }));

        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTick(shipSymbol, resolver).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledAtThrowsExceptionWhenAlgorithmThrowsException() {
        Ship ship = MotherShip.excavator();
        String shipSymbol = ship.getSymbol();

        when(fleetApi.getMyShip(shipSymbol)).thenReturn(new GetMyShip200Response().data(ship));

        AwaitableAnswer<Object> answer = AwaitableAnswer.await(() -> {
            throw new RuntimeException();
        });

        when(algorithm.decideBehaviour(ship)).thenAnswer(answer);

        OffsetDateTime timestamp = OffsetDateTime.now();
        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTickAt(shipSymbol, resolver, timestamp).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        answer.release();

        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledAtThrowsExceptionWhenBehaviourThrowsException() {
        Ship ship = MotherShip.excavator();
        String shipSymbol = ship.getSymbol();

        when(fleetApi.getMyShip(shipSymbol)).thenReturn(new GetMyShip200Response().data(ship));
        when(algorithm.decideBehaviour(ship)).thenReturn(ShipBehaviour.ofSupplier(() -> {
            throw new RuntimeException();
        }));

        OffsetDateTime timestamp = OffsetDateTime.now();
        CompletableFuture<ShipBehaviourResult> future = sut.scheduleTickAt(shipSymbol, resolver, timestamp).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }
}
