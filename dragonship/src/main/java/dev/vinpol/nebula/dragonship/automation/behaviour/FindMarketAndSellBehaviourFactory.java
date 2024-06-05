package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourLeafs.*;

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

            List<Waypoint> waypoints = getCurrentMarketsInSystem(currentLocation.toSystemSymbol());

            if (waypoints.isEmpty()) {
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.NO_WAYPOINTS_FOUND_IN_CURRENT_SYSTEM));
            }

            MarketWaypoint waypoint = getBestMarketForCurrentCargo(ship, waypoints);

            WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(waypoint.waypoint().getSymbol());

            List<ShipBehaviour> behaviours = new ArrayList<>();
            behaviours.add(orbit());
            behaviours.add(navigate(waypointSymbol));
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

    private MarketWaypoint getBestMarketForCurrentCargo(Ship ship, List<Waypoint> waypoints) {
        // sort waypoints so we navigate to the closest
        // TODO: test if actually the closest market is selected
        return waypoints
            .stream()
            .map(w -> {
                Market market = systemsApi.getMarket(w.getSystemSymbol(), w.getSymbol()).getData();

                int score = calculateSellScore(market, ship.getCargo());
                if (score <= 0) {
                    return null;
                }

                return new MarketWaypoint(w, market, score);
            })
            .filter(Objects::nonNull)
            .min(
                // TODO: test if this works; canSellScore should be high while the distance should be the lowest
                Comparator.comparing(MarketWaypoint::canSellScore)
                    .thenComparing(MarketWaypoint::waypoint, new Comparator<>() {
                        @Override
                        public int compare(Waypoint o1, Waypoint o2) {
                            return (int) calculator.calculateDistance(toCoordinate(o1), toCoordinate(o2));
                        }

                        private Coordinate toCoordinate(Waypoint waypoint) {
                            return new Coordinate(waypoint.getX(), waypoint.getY());
                        }
                    })
            )
            .orElseThrow();
    }

    private @NotNull List<Waypoint> getCurrentMarketsInSystem(SystemSymbol currentLocation) {
        return PageIterator.stream((req -> {
            GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(currentLocation.system(), req.page(), req.size(), null, WaypointTraitSymbol.MARKETPLACE);
            return new Page<>(response.getData(), response.getMeta().getTotal());
        })).toList();
    }

    private int calculateSellScore(Market market, ShipCargo cargo) {
        return cargo.getInventory()
            .stream()
            .mapToInt(i -> (int) streamImports(market).filter(symbol -> Objects.equals(symbol, i.getSymbol())).count() * i.getUnits())
            .sum();
    }

    private static Stream<TradeSymbol> streamImports(Market market) {
        return market.getImports().stream()
            .map(TradeGood::getSymbol);
    }

    private record MarketWaypoint(Waypoint waypoint, Market market, int canSellScore) {

    }
}
