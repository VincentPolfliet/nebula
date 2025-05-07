package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.nebula.dragonship.sdk.ShipExtractionUtil;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static dev.vinpol.nebula.dragonship.sdk.ShipCargoUtil.cargoItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ExtractionBehaviourFactoryTest {

    FleetApi fleetApi;
    ShipEventNotifier shipEventNotifier;
    ExtractionBehaviourFactory sut;

    @BeforeEach
    void setup() {
        fleetApi = mock(FleetApi.class);
        shipEventNotifier = mock(ShipEventNotifier.class);

        sut = new ExtractionBehaviourFactory(fleetApi, shipEventNotifier);
    }

    @Test
    void doesExtraction() {
        OffsetDateTime cooldownTimestamp = OffsetDateTime.now().plusSeconds(60);

        Ship ship = MotherShip.excavator();
        Ship expected = ShipCloner.clone(ship)
            .cooldown(new Cooldown()
                .totalSeconds(60)
                .remainingSeconds(59)
                .expiration(cooldownTimestamp)
            )
            .cargo(
                new ShipCargo()
                    // consider it full
                    .units(ship.getCargo().getCapacity())
                    .capacity(ship.getCargo().getCapacity())
            );

        ExtractResources201ResponseData responseData = new ExtractResources201ResponseData();

        responseData.extraction(new Extraction()
            .yield(ShipExtractionUtil.extractionYield(TradeSymbol.COPPER_ORE, expected.getCargo().getCapacity()))
        );

        responseData.cooldown(
            expected.getCooldown()
        );

        responseData.cargo(
            expected.getCargo()
        );

        when(fleetApi.extractResources(ship.getSymbol(), new ExtractResourcesRequest()))
            .thenReturn(new ExtractResources201Response().data(responseData));

        ShipBehaviour behaviour = sut.create();
        ShipBehaviorResult result = behaviour.update(ship);

        assertThat(result.isWaitUntil()).isTrue();
        assertThat(result)
            .isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
                assertThat(waitUntil.timestamp()).isEqualTo(cooldownTimestamp);
            });

        verify(shipEventNotifier).setCargoFull(ship.getSymbol());
        verify(shipEventNotifier).setWaitUntilCooldown(ship.getSymbol(), cooldownTimestamp);
    }

    @Test
    void cargoIsFull() {
        Ship ship = MotherShip.excavator()
            .cargo(
                new ShipCargo()
                    .capacity(1)
                    .units(1)
                    .addInventoryItem(cargoItem(TradeSymbol.COPPER))
            );

        ShipBehaviour behaviour = sut.create();
        ShipBehaviorResult result = behaviour.update(ship);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.hasFailedWithReason(FailureReason.CARGO_IS_FULL)).isTrue();

        verifyNoInteractions(fleetApi);
        verifyNoInteractions(shipEventNotifier);
    }

    @Test
    void shipIsInTransit() {
        Ship ship = MotherShip.excavator()
            .nav(
                new ShipNav()
                    .status(ShipNavStatus.IN_TRANSIT)
            );

        ShipBehaviour behaviour = sut.create();
        ShipBehaviorResult result = behaviour.update(ship);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.hasFailedWithReason(FailureReason.NOT_IN_ORBIT)).isTrue();

        verifyNoInteractions(fleetApi);
        verifyNoInteractions(shipEventNotifier);
    }

    @Test
    void shipIsDocked() {
        Ship ship = MotherShip.excavator()
            .nav(
                new ShipNav()
                    .status(ShipNavStatus.DOCKED)
            );

        ShipBehaviour behaviour = sut.create();
        ShipBehaviorResult result = behaviour.update(ship);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.hasFailedWithReason(FailureReason.NOT_IN_ORBIT)).isTrue();

        verifyNoInteractions(fleetApi);
        verifyNoInteractions(shipEventNotifier);
    }


    @Test
    void shipHasActiveCooldown() {
        Ship ship = MotherShip.excavator()
            .cooldown(new Cooldown()
                .totalSeconds(60)
                .remainingSeconds(59)
                .expiration(OffsetDateTime.now())
            );

        ShipBehaviour behaviour = sut.create();
        ShipBehaviorResult result = behaviour.update(ship);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.hasFailedWithReason(FailureReason.ACTIVE_COOLDOWN)).isTrue();

        verifyNoInteractions(fleetApi);
        verifyNoInteractions(shipEventNotifier);
    }
}
