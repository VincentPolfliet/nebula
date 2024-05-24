package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ShipBehaviourScheduler;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.command.MaxRescheduleCountExceededException;
import dev.vinpol.nebula.dragonship.automation.command.ShipCommander;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.MotherShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class ShipCommanderTest {

    ShipBehaviourScheduler scheduler;
    FleetApi fleetApi;

    ShipCommander sut;

    @BeforeEach
    void setup() {
        scheduler = mock(ShipBehaviourScheduler.class);
        fleetApi = mock(FleetApi.class);

        sut = new ShipCommander(scheduler, fleetApi);
    }

    @Test
    void testThrowsExceptionWhenShipIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> {
                sut.command(null);
            });
    }

    @Test
    void doNotScheduleWhenAlreadyScheduled() {
        Ship ship = MotherShip.excavator();

        when(scheduler.isTickScheduled(ship)).thenReturn(true);

        CompletionStage<?> stage = sut.command(ship);

        assertThat(stage).isCompleted();

        verify(scheduler, times(0)).scheduleTick(eq(ship.getSymbol()), any(), any());
        verify(scheduler, times(0)).scheduleTickAt(eq(ship.getSymbol()), any(), any(), any());
    }

    @Test
    void scheduleShipWithSuccessAndDone() {
        Ship ship = MotherShip.excavator();

        when(scheduler.isTickScheduled(ship)).thenReturn(false);

        when(scheduler.scheduleTick(eq(ship.getSymbol()), any(), any()))
            .thenReturn(
            CompletableFuture.completedFuture(ShipBehaviourResult.success()),
            CompletableFuture.completedFuture(ShipBehaviourResult.done())
        );

        CompletionStage<?> stage = sut.command(ship);

        assertThat(stage).succeedsWithin(Duration.ofSeconds(1));

        verify(scheduler, times(2)).scheduleTick(eq(ship.getSymbol()), any(), any());
    }

    @Test
    void scheduleShipWithWaitUntil() {
        Ship ship = MotherShip.excavator();

        OffsetDateTime waitUntilTimestamp = OffsetDateTime.now().plusSeconds(10);

        when(scheduler.isTickScheduled(ship)).thenReturn(false);

        when(scheduler.scheduleTick(eq(ship.getSymbol()), any(), any())).thenReturn(
            CompletableFuture.completedFuture(ShipBehaviourResult.success()),
            CompletableFuture.completedFuture(ShipBehaviourResult.waitUntil(waitUntilTimestamp)),
            CompletableFuture.completedFuture(ShipBehaviourResult.done())
        );

        when(scheduler.scheduleTickAt(eq(ship.getSymbol()), any(), any(), eq(waitUntilTimestamp))).thenReturn(CompletableFuture.completedFuture(ShipBehaviourResult.success()));

        CompletionStage<?> stage = sut.command(ship);

        assertThat(stage)
            .succeedsWithin(Duration.ofSeconds(1));

        InOrder inOrder = inOrder(scheduler);

        inOrder.verify(scheduler, times(2)).scheduleTick(eq(ship.getSymbol()), any(), any());
        inOrder.verify(scheduler).scheduleTickAt(eq(ship.getSymbol()), any(), any(), eq(waitUntilTimestamp));
        inOrder.verify(scheduler).scheduleTick(eq(ship.getSymbol()), any(), any());
    }

    @Test
    void scheduleShipWithFailThrowsException() {
        Ship ship = MotherShip.excavator();

        when(scheduler.isTickScheduled(ship)).thenReturn(false);

        when(scheduler.scheduleTick(eq(ship.getSymbol()), any(), any())).thenReturn(
            CompletableFuture.completedFuture(ShipBehaviourResult.failure("fail this"))
        );

        CompletionStage<?> stage = sut.command(ship);

        assertThat(stage)
            .failsWithin(Duration.ofSeconds(1));
    }


    @Test
    void testMaxReschedule() {
        Ship ship = MotherShip.excavator();
        ShipCommander commander = new ShipCommander(scheduler, fleetApi, new ShipCommander.Config(1));

        when(scheduler.isTickScheduled(ship)).thenReturn(false);

        when(scheduler.scheduleTick(eq(ship.getSymbol()), any(), any())).thenReturn(
            CompletableFuture.completedFuture(ShipBehaviourResult.success())
        );

        // override the next behaviour to never send a "Done" event
        CompletableFuture<?> command = commander.command(ship, ShipBehaviour.ofResult(ShipBehaviourResult.success())).toCompletableFuture();

        await().until(command::isDone);
        assertThat(command).isCompletedExceptionally();

        assertThatThrownBy(() -> command.getNow(null))
            .hasRootCauseInstanceOf(MaxRescheduleCountExceededException.class);
    }
}


