package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.DockShip200Response;
import dev.vinpol.spacetraders.sdk.models.ShipNavModifiedResponseData;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipNav;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockBehaviourFactory implements ShipBehaviourFactory {


    private final Logger logger = LoggerFactory.getLogger(DockBehaviourFactory.class);
    private final FleetApi fleetApi;

    public DockBehaviourFactory(FleetApi fleetApi) {
        this.fleetApi = fleetApi;
    }

    @Override
    public ShipBehaviour create() {
        return new ShipBehaviour() {
            @Override
            public String getName() {
                return "dock";
            }

            @Override
            public ShipBehaviourResult update(Ship ship) {
                if (ship.isDocked()) {
                    return ShipBehaviourResult.failure(FailureReason.DOCKED);
                }

                if (ship.isInTransit()) {
                    return ShipBehaviourResult.failure(FailureReason.IN_TRANSIT);
                }

                DockShip200Response dockResponse = fleetApi.dockShip(ship.getSymbol());
                ShipNavModifiedResponseData data = dockResponse.getData();
                ShipNav nav = data.getNav();
                ship.setNav(nav);
                return ShipBehaviourResult.done();
            }
        };
    }
}
