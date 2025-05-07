package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ScheduledExecutor;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ShipBehaviourScheduler;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.Done;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.spacetraders.sdk.models.MotherShip;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

class ShipBehaviourSchedulerTest {

    ScheduledExecutor timer;
    Executor worker;

    ShipBehaviourScheduler sut;

    private String shipSymbol;
    private Ship ship;

    private Function<String, Ship> shipFunction;

    private record DirectThreadRunner() implements Executor {

        @Override
        public void execute(@NotNull Runnable command) {
            command.run();
        }
    }

    @BeforeEach
    void setup() {
        worker = spy(new DirectThreadRunner());
        timer = spy(ScheduledExecutor.ofExecutor(worker));
        sut = new ShipBehaviourScheduler(timer, worker);

        ship = MotherShip.excavator();
        shipSymbol = ship.getSymbol();
        shipFunction = _ -> ship;
    }

    @Test
    void scheduleTick() {
        ShipBehaviorResult result = ShipBehaviorResult.done();

        CompletableFuture<ShipBehaviorResult> stage = sut.scheduleTick(shipSymbol, shipFunction, ofResult(result));

        ShipBehaviorResult actual = stage.resultNow();
        assertThat(actual).isInstanceOf(Done.class);
        assertThat(sut.isTickScheduled(ship)).isFalse();
    }

    private static Function<Ship, ShipBehaviour> ofResult(ShipBehaviorResult result) {
        return _ -> ShipBehaviour.ofResult(result);
    }

    @Test
    void scheduleTickAt() {
        OffsetDateTime timestamp = OffsetDateTime.now();

        ShipBehaviorResult result = ShipBehaviorResult.done();
        CompletableFuture<ShipBehaviorResult> stage = sut.scheduleTickAt(shipSymbol, shipFunction, ofResult(result), timestamp).toCompletableFuture();

        await().until(stage::isDone);
        ShipBehaviorResult actual = stage.resultNow();
        assertThat(actual).isInstanceOf(Done.class);
        assertThat(sut.isTickScheduled(ship)).isFalse();
    }

    @Test
    void scheduleTickThrowsExceptionWhenParamsAreNull() {
        assertThatNullPointerException()
            .isThrownBy(() -> {
                sut.scheduleTick(null, _ -> new Ship(), _ -> ShipBehaviour.finished());
            });

        assertThatNullPointerException()
            .isThrownBy(() -> {
                sut.scheduleTick("symbol", _ -> new Ship(), null);
            });

        assertThatNullPointerException()
            .isThrownBy(() -> {
                sut.scheduleTick("symbol", null, _ -> ShipBehaviour.finished());
            });
    }

    @Test
    void isTickScheduledReturnsFalseWhenShipIsNull() {
        assertFalse(sut.isTickScheduled((Ship) null));
    }

    @Test
    void isTickScheduledReturnsTrueWhenScheduledAtNow() {
        OffsetDateTime timestamp = OffsetDateTime.now().plusNanos(300);
        CompletableFuture<ShipBehaviorResult> future = sut.scheduleTickAt(shipSymbol, shipFunction, ofResult(ShipBehaviorResult.done()), timestamp).toCompletableFuture();

        assertTrue(sut.isTickScheduled(ship));
        await().until(future::isDone);
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledThrowsExceptionWhenAlgorithmThrowsException() {
        CompletableFuture<ShipBehaviorResult> future = sut.scheduleTick(shipSymbol, shipFunction, _ -> {
            assertTrue(sut.isTickScheduled(ship));
            throw new RuntimeException();
        });

        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledThrowsExceptionWhenBehaviourThrowsException() {
        CompletableFuture<ShipBehaviorResult> future = sut.scheduleTick(shipSymbol, shipFunction, _ -> {
            assertTrue(sut.isTickScheduled(ship));
            throw new RuntimeException();
        });

        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void tickScheduledAtThrowsExceptionWhenBehaviourThrowsException() {
        OffsetDateTime timestamp = OffsetDateTime.now();
        CompletableFuture<ShipBehaviorResult> future = sut.scheduleTickAt(shipSymbol, shipFunction, _ -> {
            assertTrue(sut.isTickScheduled(ship));
            throw new RuntimeException();
        }, timestamp);

        await().until(future::isDone);
        assertThat(future).isCompletedExceptionally();
        assertFalse(sut.isTickScheduled(ship));
    }

    @Test
    void getCurrentBehaviourViaScheduleTickAt() {
        OffsetDateTime future = OffsetDateTime.now().plusSeconds(20);
        sut.scheduleTickAt(shipSymbol, _ -> ship, _ -> ShipBehaviour.finished(), future);
        Optional<ShipBehaviour> currentBehaviourOptional = sut.getCurrentBehaviour(shipSymbol);

        assertThat(currentBehaviourOptional).isPresent();

        ShipBehaviour currentBehaviour = currentBehaviourOptional.get();
        assertThat(currentBehaviour.update(ship)).satisfies(result -> {
            assertThat(result.isWaitUntil()).isTrue();

            WaitUntil waitUntil = (WaitUntil) result;
            assertThat(waitUntil.timestamp()).isEqualTo(future);
        });
    }

    @Test
    void getCurrentBehaviourViaScheduleTick() {
        var behaviour = ShipBehaviour.ofResult(ShipBehaviorResult.done());

        CompletableFuture<ShipBehaviorResult> future = sut.scheduleTick(shipSymbol, shipFunction, _ -> {
            assertTrue(sut.isTickScheduled(ship));

            Optional<ShipBehaviour> currentBehaviourOptional = sut.getCurrentBehaviour(shipSymbol);

            assertThat(currentBehaviourOptional).isPresent();
            ShipBehaviour currentBehaviour = currentBehaviourOptional.get();
            assertThat(currentBehaviour).isEqualTo(behaviour);

            return behaviour;
        });

        await().until(future::isDone);
    }

    @Test
    void cancelTick() {
        CompletableFuture<ShipBehaviorResult> future = sut.scheduleTick(shipSymbol, shipFunction, _ -> {
            assertThat(sut.isTickScheduled(shipSymbol)).isTrue();

            var cancelFuture = CompletableFuture.runAsync(() -> {
                sut.cancelTick(shipSymbol);
                assertThat(sut.isTickScheduled(shipSymbol)).isFalse();
            });

            await().until(cancelFuture::isDone);
            return ShipBehaviour.ofResult(ShipBehaviorResult.done());
        });

        await().until(future::isDone);
        future.join();
    }
}
