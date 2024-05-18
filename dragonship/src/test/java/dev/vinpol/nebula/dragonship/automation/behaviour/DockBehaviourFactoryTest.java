package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DockBehaviourFactoryTest {

    FleetApi fleetApi;
    DockBehaviourFactory sut;

    @BeforeEach
    void setup() {
        fleetApi = mock(FleetApi.class);

        sut = new DockBehaviourFactory(fleetApi);
    }

    @Test
    void dockedShip() {
        Ship dockedShip = MotherShip.excavator();
        ShipNav nav = dockedShip.getNav();
        nav.status(ShipNavStatus.DOCKED);

        ShipBehaviour behaviour = sut.create();

        ShipBehaviourResult result = behaviour.update(dockedShip);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.hasFailedWithReason(FailureReason.DOCKED)).isTrue();
        verifyNoInteractions(fleetApi);
    }

    @Test
    void inTransitShip() {
        Ship transitShip = MotherShip.excavator();
        ShipNav nav = transitShip.getNav();
        nav.status(ShipNavStatus.IN_TRANSIT);

        ShipBehaviour behaviour = sut.create();

        ShipBehaviourResult result = behaviour.update(transitShip);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.hasFailedWithReason(FailureReason.IN_TRANSIT)).isTrue();
        verifyNoInteractions(fleetApi);
    }

    @Test
    void inOrbitShip() {
        Ship inOrbitShip = MotherShip.excavator();
        ShipNav nav = inOrbitShip.getNav();
        nav.status(ShipNavStatus.IN_ORBIT);

        ShipNav dockedNav = ShipCloner.clone(inOrbitShip).getNav();
        dockedNav.status(ShipNavStatus.DOCKED);

        when(fleetApi.dockShip(inOrbitShip.getSymbol())).thenReturn(
            new DockShip200Response()
                .data(new ShipNavModifiedResponseData().nav(dockedNav))
        );

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(inOrbitShip);

        assertThat(result.isDone()).isTrue();
        assertThat(inOrbitShip.getNav()).isEqualTo(dockedNav);
    }
}
