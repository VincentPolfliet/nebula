package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviourFactory;
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
        return (ship) -> {
            // orbit ship, so that it can fly to asteroid or mine the asteroid
            if (OrbitBehaviourFactory.this.isInOrbit(ship)) {
                return ShipBehaviourResult.done();
            }

            OrbitShip200Response orbit = fleetApi.orbitShip(ship.getSymbol());
            ship.setNav(orbit.getData().getNav());
            return ShipBehaviourResult.done();
        };
    }

    private boolean isInOrbit(Ship ship) {
        return ship.getNav().isInOrbit();
    }
}
