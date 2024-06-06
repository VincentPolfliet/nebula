package dev.vinpol.nebula.dragonship.automation.behaviour.market;

import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class MarketFinder {

    private final SystemsApi systemsApi;
    private final TravelFuelAndTimerCalculator calculator;

    public MarketFinder(SystemsApi systemsApi, TravelFuelAndTimerCalculator calculator) {
        this.systemsApi = systemsApi;
        this.calculator = calculator;
    }

    public MarketWaypoint getBestMarketForCurrentCargo(Ship ship, Waypoint currentLocation, List<Waypoint> waypoints) {
        // sort waypoints so we navigate to the closest
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
            .max(
                Comparator.comparing(MarketWaypoint::canSellScore)
                    .thenComparing(MarketWaypoint::waypoint, new Comparator<>() {
                        @Override
                        public int compare(Waypoint o1, Waypoint o2) {
                            double distanceToO1 = calculator.calculateDistance(toCoordinate(currentLocation), toCoordinate(o1));
                            double distanceToO2 = calculator.calculateDistance(toCoordinate(currentLocation), toCoordinate(o2));
                            return Double.compare(distanceToO2, distanceToO1);
                        }

                        private Coordinate toCoordinate(Waypoint waypoint) {
                            return new Coordinate(waypoint.getX(), waypoint.getY());
                        }
                    })
            )
            .orElse(null);
    }

    static int calculateSellScore(Market market, ShipCargo cargo) {
        return cargo.getInventory()
            .stream()
            .mapToInt(i -> (int) streamImports(market).filter(symbol -> Objects.equals(symbol, i.getSymbol())).count() * i.getUnits())
            .sum();
    }

    private static Stream<TradeSymbol> streamImports(Market market) {
        return market.getImports().stream()
            .map(TradeGood::getSymbol);
    }
}
