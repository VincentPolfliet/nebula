package dev.vinpol.nebula.dragonship.automation.behaviour.market;

import dev.vinpol.nebula.dragonship.automation.behaviour.AutomationFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour.safe;
import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourLeafs.*;

public class FindMarketAndSellBehaviourFactory implements ShipBehaviourFactory {

    private final AutomationFactory shipBehaviourFactoryCreator;
    private final SystemsApi systemsApi;
    private final TravelFuelAndTimerCalculator calculator;

    public FindMarketAndSellBehaviourFactory(AutomationFactory shipBehaviourFactoryCreator, SystemsApi systemsApi, TravelFuelAndTimerCalculator calculator) {
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

            WaypointSymbol currentLocationSymbol = WaypointSymbol.tryParse(ship.getNav().getWaypointSymbol());
            List<Waypoint> waypoints = getCurrentMarketsInSystem(currentLocationSymbol.toSystemSymbol());

            if (waypoints.isEmpty()) {
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.NO_WAYPOINTS_FOUND_IN_CURRENT_SYSTEM));
            }

            Waypoint currentLocation = systemsApi.getWaypoint(ship.getNav().getSystemSymbol(), ship.getNav().getWaypointSymbol()).getData();
            MarketWaypoint waypoint = getBestMarketForCurrentCargo(ship, currentLocation, waypoints);

            WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(waypoint.waypoint().getSymbol());

            List<ShipBehaviour> behaviours = new ArrayList<>();
            behaviours.add(orbit());
            behaviours.add(safe(navigate(waypointSymbol)));
            behaviours.add(dock());

            ShipCargo cargo = ship.getCargo();
            List<ShipCargoItem> inventory = cargo.getInventory();
            List<TradeSymbol> imports = streamImports(waypoint.market()).toList();

            for (ShipCargoItem shipCargoItem : inventory) {
                if (imports.contains(shipCargoItem.getSymbol())) {
                    behaviours.add(sellCargo(shipCargoItem.getSymbol(), shipCargoItem.getUnits()));
                }
            }

            behaviours.add(refuel());

            return shipBehaviourFactoryCreator.treeOf(behaviours);
        });
    }

    private MarketWaypoint getBestMarketForCurrentCargo(Ship ship, Waypoint currentLocation, List<Waypoint> waypoints) {
        return new MarketFinder(systemsApi, calculator).getBestMarketForCurrentCargo(ship, currentLocation, waypoints);
    }

    private @NotNull List<Waypoint> getCurrentMarketsInSystem(SystemSymbol currentLocation) {
        return PageIterator.stream((req -> {
            GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(currentLocation.system(), req.page(), req.size(), null, WaypointTraitSymbol.MARKETPLACE);
            return new Page<>(response.getData(), response.getMeta().getTotal());
        })).toList();
    }

    private static Stream<TradeSymbol> streamImports(Market market) {
        return market.getImports().stream()
            .map(TradeGood::getSymbol);
    }
}
