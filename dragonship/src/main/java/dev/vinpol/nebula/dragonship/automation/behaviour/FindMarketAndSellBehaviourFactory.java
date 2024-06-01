package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;

import java.util.ArrayList;
import java.util.List;

import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourLeafs.*;
import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourSequence.sequence;

public class FindMarketAndSellBehaviourFactory implements ShipBehaviourFactory {

    private final ShipBehaviourFactoryCreator shipBehaviourFactoryCreator;
    private final SystemsApi systemsApi;

    public FindMarketAndSellBehaviourFactory(ShipBehaviourFactoryCreator shipBehaviourFactoryCreator, SystemsApi systemsApi) {
        this.shipBehaviourFactoryCreator = shipBehaviourFactoryCreator;
        this.systemsApi = systemsApi;
    }

    @Override
    public ShipBehaviour create() {
        return ShipBehaviour.ofFunction((ship) -> {
            if (ship.getCargo().isEmpty()) {
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.NO_CARGO_TO_SELL));
            }

            ShipNavRouteWaypoint currentLocation = ship.getNav()
                .getRoute()
                .getDestination();

            GetSystemWaypoints200Response marketPlacesResponse = systemsApi.getSystemWaypoints(currentLocation.getSystemSymbol(), 1, 10, null, WaypointTraitSymbol.MARKETPLACE);
            List<Waypoint> waypoints = marketPlacesResponse.getData();

            if (waypoints.isEmpty()) {
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.NO_WAYPOINTS_FOUND_IN_CURRENT_SYSTEM));
            }

            Waypoint waypoint = waypoints.getFirst();
            WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(waypoint.getSymbol());

            List<ShipBehaviour> behaviours = new ArrayList<>();
            behaviours.add(orbit());
            behaviours.add(navigate(waypointSymbol));
            behaviours.add(dock());

            ShipCargo cargo = ship.getCargo();
            List<ShipCargoItem> inventory = cargo.getInventory();

            for (ShipCargoItem shipCargoItem : inventory) {
                behaviours.add(sellCargo(shipCargoItem.getSymbol(), shipCargoItem.getUnits()));
            }

            behaviours.add(refuel());

            return shipBehaviourFactoryCreator.treeOf(behaviours);
        });
    }
}
