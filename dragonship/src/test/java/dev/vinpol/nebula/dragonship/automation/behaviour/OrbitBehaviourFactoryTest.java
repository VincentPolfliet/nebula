package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrbitBehaviourFactoryTest {

    FleetApi fleetApi;
    OrbitBehaviourFactory sut;

    @BeforeEach
    void setup() {
        fleetApi = mock(FleetApi.class);
        sut = new OrbitBehaviourFactory(fleetApi, mock(ShipEventNotifier.class));

        when(fleetApi.orbitShip(any()))
            .thenReturn(new OrbitShip200Response()
                .data(new ShipNavModifiedResponseData()
                    .nav(new ShipNav().status(ShipNavStatus.IN_ORBIT))
                )
            );
    }

    @Test
    void shipOrbit() {
        for (ShipNavStatus shipNavStatus : ShipNavStatus.values()) {
            Ship ship = MotherShip.excavator()
                .nav(new ShipNav()
                    .status(shipNavStatus)
                );

            ShipBehaviour behaviour = sut.create();
            ShipBehaviorResult result = behaviour.update(ship);

            assertThat(result.isDone()).isTrue();
            assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.IN_ORBIT);
        }
    }
}
