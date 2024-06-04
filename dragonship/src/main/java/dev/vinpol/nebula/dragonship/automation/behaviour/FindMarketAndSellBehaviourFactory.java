package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourLeafs.*;
import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourSequence.sequence;

public class FindMarketAndSellBehaviourFactory implements ShipBehaviourFactory {

    private final ShipBehaviourFactoryCreator shipBehaviourFactoryCreator;
    private final SystemsApi systemsApi;
    private final TravelFuelAndTimerCalculator calculator;

    public FindMarketAndSellBehaviourFactory(ShipBehaviourFactoryCreator shipBehaviourFactoryCreator, SystemsApi systemsApi, TravelFuelAndTimerCalculator calculator) {
        this.shipBehaviourFactoryCreator = shipBehaviourFactoryCreator;
        this.systemsApi = systemsApi;
        this.calculator = calculator;
    }

    @Override
    public ShipBehaviour create() {
        return ShipBehaviour.ofFunction((ship) -> {
            if (ship.getCargo().isEmpty()) {
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.NO_CARGO_TO_SELL));
            }

            WaypointSymbol currentLocation = WaypointSymbol.tryParse(ship.getNav().getWaypointSymbol());

            List<Waypoint> waypoints = PageIterator.stream((req -> {
                GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(currentLocation.system(), req.page(), req.size(), null, WaypointTraitSymbol.MARKETPLACE);
                return new Page<>(response.getData(), response.getMeta().getTotal());
            })).toList();

            if (waypoints.isEmpty()) {
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.NO_WAYPOINTS_FOUND_IN_CURRENT_SYSTEM));
            }

            // sort waypoints so we navigate to the closest
            Waypoint waypoint = waypoints
                .stream()
                .min(new Comparator<>() {
                    @Override
                    public int compare(Waypoint o1, Waypoint o2) {
                        return (int) calculator.calculateDistance(toCoordinate(o1), toCoordinate(o2));
                    }

                    private Coordinate toCoordinate(Waypoint waypoint) {
                        return new Coordinate(waypoint.getX(), waypoint.getY());
                    }
                })
                .orElseThrow();

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
