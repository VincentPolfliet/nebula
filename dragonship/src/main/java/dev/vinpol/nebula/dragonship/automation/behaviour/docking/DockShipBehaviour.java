package dev.vinpol.nebula.dragonship.automation.behaviour.docking;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.DockShip200Response;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipNav;
import dev.vinpol.spacetraders.sdk.models.ShipNavModifiedResponseData;

public record DockShipBehaviour(FleetApi fleetApi, ShipEventNotifier notifier) implements ShipBehaviour {
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

        notifier.setDocked(ship.getSymbol());
        return ShipBehaviourResult.done();
    }
}
