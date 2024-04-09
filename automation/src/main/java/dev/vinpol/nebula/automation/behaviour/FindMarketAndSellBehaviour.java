package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.GetSystemWaypoints200Response;
import dev.vinpol.spacetraders.sdk.models.ShipNavRouteWaypoint;
import dev.vinpol.spacetraders.sdk.models.SystemTraits;
import dev.vinpol.spacetraders.sdk.models.Waypoint;

import java.util.List;

import static dev.vinpol.nebula.automation.behaviour.tree.ShipBehaviourLeafs.*;

public class FindMarketAndSellBehaviour implements ShipBehaviourFactory {

    private final ShipBehaviourFactoryCreator shipBehaviourFactoryCreator;
    private final SystemsApi systemsApi;

    public FindMarketAndSellBehaviour(ShipBehaviourFactoryCreator shipBehaviourFactoryCreator, SystemsApi systemsApi) {
        this.shipBehaviourFactoryCreator = shipBehaviourFactoryCreator;
        this.systemsApi = systemsApi;
    }

    @Override
    public ShipBehaviour create() {
        return ShipBehaviour.ofFunction((ship) -> {
            ShipNavRouteWaypoint currentLocation = ship.getNav()
                .getRoute()
                .getDestination();

            GetSystemWaypoints200Response marketPlacesResponse = systemsApi.getSystemWaypoints(currentLocation.getSystemSymbol(), 1, 10, null, new String[]{SystemTraits.MARKETPLACE});
            List<Waypoint> waypoints = marketPlacesResponse.getData();

            if (waypoints.isEmpty()) {
                // TODO notify that no waypoint are in current system
                // FIXME this will cause the ship to do nothing in the current implementation
                return ShipBehaviour.finished();
            }

            Waypoint waypoint = waypoints.getFirst();

            return shipBehaviourFactoryCreator.sequenceOf(
                List.of(
                    navigate(WaypointSymbol.tryParse(waypoint.getSymbol())),
                    dock(),
                    refuel()
                )
            );
        });
    }
}
