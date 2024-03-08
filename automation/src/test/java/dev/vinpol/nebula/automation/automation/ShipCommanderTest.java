package dev.vinpol.nebula.automation.automation;

import dev.vinpol.nebula.automation.ShipBehaviourScheduler;
import dev.vinpol.nebula.automation.ShipCommander;
import dev.vinpol.nebula.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.ShipMother;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class ShipCommanderTest {

    ShipAlgorithmResolver resolver;

    ShipBehaviourScheduler scheduler;

    ShipCommander sut;

    @BeforeEach
    void setup() {
        resolver = mock(ShipAlgorithmResolver.class);
        scheduler = mock(ShipBehaviourScheduler.class);

        sut = new ShipCommander(resolver, scheduler);
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
        Ship ship = ShipMother.excavator();

        when(scheduler.isTickScheduled(ship)).thenReturn(true);

        CompletionStage<?> stage = sut.command(ship);

        assertThat(stage).isCompleted();

        verify(scheduler, times(0)).scheduleTick(ship, resolver);
        verify(scheduler, times(0)).scheduleTickAt(eq(ship), eq(resolver), any());
    }

    @Test
    void scheduleShipWithSuccessAndDone() {
        Ship ship = ShipMother.excavator();

        when(scheduler.isTickScheduled(ship)).thenReturn(false);

        when(scheduler.scheduleTick(ship, resolver)).thenReturn(
            CompletableFuture.completedStage(ShipBehaviourResult.success()),
            CompletableFuture.completedStage(ShipBehaviourResult.done())
        );

        CompletionStage<?> stage = sut.command(ship);

        assertThat(stage).succeedsWithin(Duration.ofSeconds(1));

        verify(scheduler, times(2)).scheduleTick(ship, resolver);
    }

    @Test
    void scheduleShipWithWaitUntil() {
        Ship ship = ShipMother.excavator();

        OffsetDateTime waitUntilTimestamp = OffsetDateTime.now().plusSeconds(10);

        when(scheduler.isTickScheduled(ship)).thenReturn(false);

        when(scheduler.scheduleTick(ship, resolver)).thenReturn(
            CompletableFuture.completedStage(ShipBehaviourResult.success()),
            CompletableFuture.completedStage(ShipBehaviourResult.waitUntil(waitUntilTimestamp)),
            CompletableFuture.completedStage(ShipBehaviourResult.done())
        );

        when(scheduler.scheduleTickAt(ship, resolver, waitUntilTimestamp)).thenReturn(CompletableFuture.completedStage(ShipBehaviourResult.success()));

        CompletionStage<?> stage = sut.command(ship);

        assertThat(stage)
            .succeedsWithin(Duration.ofSeconds(1));

        InOrder inOrder = inOrder(scheduler);

        inOrder.verify(scheduler, times(2)).scheduleTick(ship, resolver);
        inOrder.verify(scheduler).scheduleTickAt(ship, resolver, waitUntilTimestamp);
        inOrder.verify(scheduler).scheduleTick(ship, resolver);
    }

    @Test
    void scheduleShipWithFailThrowsException() {
        Ship ship = ShipMother.excavator();

        when(scheduler.isTickScheduled(ship)).thenReturn(false);

        when(scheduler.scheduleTick(ship, resolver)).thenReturn(
            CompletableFuture.completedStage(ShipBehaviourResult.failure())
        );

        CompletionStage<?> stage = sut.command(ship);

        assertThat(stage)
            .failsWithin(Duration.ofSeconds(1));
    }
}
