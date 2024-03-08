package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.DockShip200Response;
import dev.vinpol.spacetraders.sdk.models.OrbitShip200ResponseData;
import dev.vinpol.spacetraders.sdk.models.ShipNav;

public class DockBehaviourFactory implements ShipBehaviourFactory {

    private final FleetApi fleetApi;

    public DockBehaviourFactory(FleetApi fleetApi) {
        this.fleetApi = fleetApi;
    }

    @Override
    public ShipBehaviour create() {
        return ship -> {
            if (ship.isDocked()) {
                return ShipBehaviourResult.done();
            }

            if (ship.isInTransit()) {
                return ShipBehaviourResult.done();
            }

            DockShip200Response dockResponse = fleetApi.dockShip(ship.getSymbol());
            OrbitShip200ResponseData data = dockResponse.getData();
            ShipNav nav = data.getNav();
            ship.setNav(nav);
            return ShipBehaviourResult.done();
        };
    }
}
