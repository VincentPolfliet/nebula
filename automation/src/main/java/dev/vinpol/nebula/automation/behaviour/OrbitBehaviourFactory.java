package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.OrbitShip200Response;
import dev.vinpol.spacetraders.sdk.models.Ship;

public final class OrbitBehaviourFactory implements ShipBehaviourFactory {

    private final FleetApi fleetApi;

    public OrbitBehaviourFactory(FleetApi fleetApi) {
        this.fleetApi = fleetApi;
    }

    @Override
    public ShipBehaviour create() {
        return new ShipBehaviour() {
            @Override
            public String getName() {
                return "orbit";
            }

            @Override
            public ShipBehaviourResult update(Ship ship) {
                // orbit ship, so that it can fly to asteroid or mine the asteroid
                if (!ship.isDocked()) {
                    return ShipBehaviourResult.failure(FailureReason.NOT_DOCKED);
                }

                OrbitShip200Response orbit = fleetApi.orbitShip(ship.getSymbol());
                ship.setNav(orbit.getData().getNav());
                return ShipBehaviourResult.done();
            }
        };
    }
}
