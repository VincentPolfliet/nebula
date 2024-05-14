package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RefuelBehaviourTest {

    FleetApi fleetApi = mock(FleetApi.class);

    RefuelBehaviour sut;

    @BeforeEach
    void setup() {
        sut = new RefuelBehaviour(fleetApi);
    }

    @Test
    void shipIsDockedAndHasFuelSpace() {
        Ship ship = ShipMother.excavator()
            .nav(new ShipNav().status(ShipNavStatus.DOCKED))
            .fuel(new ShipFuel().current(69).capacity(100));

        Ship expected = ShipCloner.clone(ship)
            .fuel(new ShipFuel()
                .current(100)
                .capacity(100)
            );

        when(fleetApi.refuelShip(eq(ship.getSymbol()), any(RefuelShipRequest.class)))
            .thenReturn(
                new RefuelShip200Response()
                    .data(
                        new RefuelShip200ResponseData()
                            .fuel(expected.getFuel())
                    )
            );

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.isDone()).isTrue();
        assertThat(ship).isEqualTo(expected);

        ArgumentCaptor<RefuelShipRequest> requestCaptor = ArgumentCaptor.forClass(RefuelShipRequest.class);
        verify(fleetApi).refuelShip(eq(ship.getSymbol()), requestCaptor.capture());

        RefuelShipRequest request = requestCaptor.getValue();
        assertThat(request.getUnits()).isNull();
        assertThat(request.isFromCargo()).isTrue();
    }

    @Test
    void shipIsDockedAndHasNoFuelSpace() {
        Ship ship = ShipMother.excavator()
            .nav(new ShipNav().status(ShipNavStatus.DOCKED))
            .fuel(new ShipFuel().current(100).capacity(100));

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.FUEL_IS_FULL)).isTrue();
    }

    @Test
    void shipIsInOrbit() {
        Ship ship = ShipMother.excavator()
            .nav(new ShipNav().status(ShipNavStatus.IN_ORBIT))
            .fuel(new ShipFuel().current(100).capacity(100));

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.NOT_DOCKED)).isTrue();
    }

    @Test
    void shipIsInTransit() {
        Ship ship = ShipMother.excavator()
            .nav(new ShipNav().status(ShipNavStatus.IN_TRANSIT))
            .fuel(new ShipFuel().current(100).capacity(100));

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.NOT_DOCKED)).isTrue();
    }
}
