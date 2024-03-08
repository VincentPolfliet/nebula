package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.NavigateShip200ResponseData;
import dev.vinpol.spacetraders.sdk.models.NavigateShipRequest;

import java.time.OffsetDateTime;

public class NavigateBehaviourFactory implements ShipBehaviourFactory {

    private final FleetApi fleetApi;
    private final ShipEventNotifier shipEventNotifier;
    private final WaypointSymbol waypointSymbol;

    public NavigateBehaviourFactory(FleetApi fleetApi, ShipEventNotifier shipEventNotifier, WaypointSymbol waypointSymbol) {
        this.fleetApi = fleetApi;
        this.shipEventNotifier = shipEventNotifier;
        this.waypointSymbol = waypointSymbol;
    }

    @Override
    public ShipBehaviour create() {
        return (ship) -> {
            NavigateShip200ResponseData navigationResponse = fleetApi.navigateShip(
                ship.getSymbol(),
                new NavigateShipRequest()
                    .waypointSymbol(waypointSymbol.waypoint())
            ).getData();

            ship.setFuel(navigationResponse.getFuel());

            if (ship.isFuelEmpty() || ship.considerFuelEmpty(0.2)) {
                shipEventNotifier.setFuelIsAlmostEmpty(ship.getSymbol());
            }

            ship.setNav(navigationResponse.getNav());

            OffsetDateTime arrival = ship.getNav().getRoute().getArrival();
            shipEventNotifier.setWaitUntilArrival(ship.getSymbol(), arrival);
            return ShipBehaviourResult.waitUntil(arrival);
        };
    }
}
