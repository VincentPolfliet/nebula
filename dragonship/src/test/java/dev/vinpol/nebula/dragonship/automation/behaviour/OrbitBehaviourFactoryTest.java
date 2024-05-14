package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrbitBehaviourFactoryTest {

    FleetApi fleetApi;
    OrbitBehaviourFactory sut;

    @BeforeEach
    void setup() {
        fleetApi = mock(FleetApi.class);
        sut = new OrbitBehaviourFactory(fleetApi);
    }

    @Test
    void shipIsDocked() {
        Ship ship = ShipMother.excavator()
            .nav(new ShipNav()
                .status(ShipNavStatus.DOCKED)
            );

        when(fleetApi.orbitShip(ship.getSymbol()))
            .thenReturn(new OrbitShip200Response()
                .data(new ShipNavModifiedResponseData()
                    .nav(new ShipNav().status(ShipNavStatus.IN_ORBIT))
                )
            );

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.isDone()).isTrue();
        assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.IN_ORBIT);
    }

    @Test
    void shipInOrbit() {
        Ship ship = ShipMother.excavator()
            .nav(new ShipNav()
                .status(ShipNavStatus.IN_ORBIT)
            );

        Ship original = ShipCloner.clone(ship);

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.hasFailedWithReason(FailureReason.NOT_DOCKED)).isTrue();
        assertThat(ship).isEqualTo(original);
    }

    @Test
    void shipInTransit() {
        Ship ship = ShipMother.excavator()
            .nav(new ShipNav()
                .status(ShipNavStatus.IN_TRANSIT)
            );

        Ship original = ShipCloner.clone(ship);

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.hasFailedWithReason(FailureReason.NOT_DOCKED)).isTrue();
        assertThat(ship).isEqualTo(original);
    }
}
