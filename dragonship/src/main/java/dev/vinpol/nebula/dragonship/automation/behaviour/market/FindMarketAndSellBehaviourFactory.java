package dev.vinpol.nebula.dragonship.automation.behaviour.market;

import dev.vinpol.nebula.dragonship.automation.behaviour.AutomationFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.nebula.dragonship.geo.GridXY;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour.safe;
import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourLeafs.*;
import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourSequence.sequence;
import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipLeafs.isAtLocation;
import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipLeafs.isNotAtLocation;

public class FindMarketAndSellBehaviourFactory implements ShipBehaviourFactory {

    private final Logger logger = LoggerFactory.getLogger(FindMarketAndSellBehaviourFactory.class);

    private final AutomationFactory shipBehaviourFactoryCreator;
    private final SystemsApi systemsApi;
    private final TravelCostCalculator calculator;

    public FindMarketAndSellBehaviourFactory(AutomationFactory shipBehaviourFactoryCreator, SystemsApi systemsApi, TravelCostCalculator calculator) {
        this.shipBehaviourFactoryCreator = shipBehaviourFactoryCreator;
        this.systemsApi = systemsApi;
        this.calculator = calculator;
    }

    @Override
    public ShipBehaviour create() {
        return ShipBehaviour.ofFunction((ship) -> {
            if (ship.getCargo().isEmpty()) {
                return ShipBehaviour.ofResult(ShipBehaviorResult.failure(FailureReason.NO_CARGO_TO_SELL));
            }

            Waypoint currentLocation = systemsApi.getWaypoint(ship.getNav().getSystemSymbol(), ship.getNav().getWaypointSymbol()).getData();

            WaypointSymbol currentLocationSymbol = WaypointSymbol.tryParse(ship.getNav().getWaypointSymbol());
            Optional<WaypointMarketScore> bestMarketOptional = streamMarketsInSystem(currentLocationSymbol.toSystemSymbol())
                .map(w -> {
                    Market market = systemsApi.getMarket(w.getSystemSymbol(), w.getSymbol()).getData();

                    int score = calculateSellScore(market, ship.getCargo());
                    if (score <= 0) {
                        return null;
                    }

                    return new WaypointMarketScore(w, market, score);
                })
                .filter(Objects::nonNull)
                .max(
                    Comparator.comparing(WaypointMarketScore::canSellScore)
                        .thenComparing(WaypointMarketScore::waypoint, new Comparator<>() {
                            @Override
                            public int compare(Waypoint o1, Waypoint o2) {
                                double distanceToO1 = calculator.calculateDistance(toCoordinate(currentLocation), toCoordinate(o1));
                                double distanceToO2 = calculator.calculateDistance(toCoordinate(currentLocation), toCoordinate(o2));
                                return Double.compare(distanceToO2, distanceToO1);
                            }

                            private GridXY toCoordinate(Waypoint waypoint) {
                                return new GridXY(waypoint.getX(), waypoint.getY());
                            }
                        })
                );

            if (bestMarketOptional.isEmpty()) {
                return ShipBehaviour.ofResult(ShipBehaviorResult.failure(FailureReason.NO_WAYPOINTS_FOUND_IN_CURRENT_SYSTEM));
            }

            WaypointMarketScore bestMarket = bestMarketOptional.get();

            logger.info("Sending '{}' to market '{}' (score: {})", currentLocation.getSymbol(), bestMarket.waypoint().getSymbol(), bestMarket.canSellScore());

            WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(bestMarket.waypoint().getSymbol());

            List<ShipBehaviour> behaviours = new ArrayList<>();
            behaviours.add(orbit());
            behaviours.add(
                sequence(
                    isNotAtLocation(waypointSymbol),
                    navigate(waypointSymbol)
                )
            );

            behaviours.add(dock());

            ShipCargo cargo = ship.getCargo();
            List<ShipCargoItem> inventory = cargo.getInventory();
            List<TradeSymbol> imports = streamImports(bestMarket.market()).toList();

            for (ShipCargoItem shipCargoItem : inventory) {
                if (imports.contains(shipCargoItem.getSymbol())) {
                    behaviours.add(sellCargo(shipCargoItem.getSymbol(), shipCargoItem.getUnits()));
                }
            }

            behaviours.add(safe(refuel()));

            return shipBehaviourFactoryCreator.treeOf(behaviours);
        });
    }

    private @NotNull StreamEx<Waypoint> streamMarketsInSystem(SystemSymbol currentLocation) {
        var innerStream = PageIterator.stream(PageIterator.INITIAL_PAGE, PageIterator.MAX_SIZE, (req -> {
            GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(currentLocation.system(), req.page(), req.size(), null, WaypointTraitSymbol.MARKETPLACE);
            return new Page<>(response.getData(), response.getMeta().total());
        }));

        return StreamEx.of(innerStream);
    }

    private static Stream<TradeSymbol> streamImports(Market market) {
        return market.getImports().stream()
            .map(TradeGood::getSymbol);
    }

    static int calculateSellScore(Market market, ShipCargo cargo) {
        return cargo.getInventory()
            .stream()
            .mapToInt(i -> (int) streamImports(market).filter(symbol -> Objects.equals(symbol, i.getSymbol())).count() * i.getUnits())
            .sum();
    }
}
