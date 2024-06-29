package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.geo.GridXY;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import org.jgrapht.Graph;
import org.jgrapht.graph.Multigraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static dev.vinpol.nebula.dragonship.geo.GridXY.toCoordinate;

public class RouteGraphCalculator {

    private final Logger logger = LoggerFactory.getLogger(RouteGraphCalculator.class);

    private final TravelCostCalculator calculator;
    private final Config config;

    public RouteGraphCalculator(TravelCostCalculator calculator, Config config) {
        this.calculator = calculator;
        this.config = config;
    }

    public Graph<String, TravelEdge> getGraph(Set<Waypoint> waypoints) {
        Multigraph<String, TravelEdge> g = new Multigraph<>(TravelEdge.class);

        for (Waypoint w : waypoints) {
            g.addVertex(w.getSymbol());
        }

        for (Waypoint last : waypoints) {
            for (Waypoint next : waypoints) {
                addEdge(g, last, next);
            }
        }

        return g;
    }

    private void addEdge(Graph<String, TravelEdge> graph, Waypoint origin, Waypoint target) {
        logger.trace("trying to add {} -> {}", origin.getSymbol(), target.getSymbol());
        if (Objects.equals(origin, target)) {
            logger.trace("origin and target are the same: org: {}, target: {}", origin.getSymbol(), target.getSymbol());
            return;
        }

        GridXY originCoordinate = toCoordinate(origin);
        GridXY targetCoordinate = toCoordinate(target);

        Map<ShipNavFlightMode, Long> fuel = calculateFuel(originCoordinate, targetCoordinate);
        Map<ShipNavFlightMode, Double> time = calculateTime(originCoordinate, targetCoordinate);

        TravelEdge edge = new TravelEdge(
            origin,
            target,
            fuel,
            time,
            calculator.calculateDistance(originCoordinate, targetCoordinate)
        );

        boolean result = graph.addEdge(origin.getSymbol(), target.getSymbol(), edge);

        if (result) {
            logger.trace("added {} -> {}", origin.getSymbol(), target.getSymbol());
        } else {
            throw new IllegalStateException("%s -> %s couldn't be added to the current graph".formatted(origin.getSymbol(), target.getSymbol()));
        }
    }

    private Map<ShipNavFlightMode, Long> calculateFuel(GridXY origin, GridXY target) {
        Map<ShipNavFlightMode, Long> timeCost = calculator.calculateFuel(origin, target);
        Map<ShipNavFlightMode, Long> modifiedWithWeight = new HashMap<>();

        for (Map.Entry<ShipNavFlightMode, Long> entry : timeCost.entrySet()) {
            modifiedWithWeight.put(entry.getKey(), entry.getValue() * Math.min(config.getFuelWeight(), 1));
        }

        return modifiedWithWeight;
    }


    private Map<ShipNavFlightMode, Double> calculateTime(GridXY origin, GridXY target) {
        Map<ShipNavFlightMode, Double> timeCost = calculator.calculateTime(origin, target, config.getEngineSpeed());
        Map<ShipNavFlightMode, Double> modifiedWithWeight = new HashMap<>();

        for (Map.Entry<ShipNavFlightMode, Double> entry : timeCost.entrySet()) {
            modifiedWithWeight.put(entry.getKey(), entry.getValue() * Math.min(config.getTimeWeight(), 1));
        }

        return modifiedWithWeight;
    }

    public static final class Config {
        private final int engineSpeed;

        private ShipNavFlightMode mode;

        private int fuelWeight = 1;
        private int timeWeight = 1;

        public Config(int engineSpeed) {
            this.engineSpeed = engineSpeed;
        }

        public int getEngineSpeed() {
            return engineSpeed;
        }

        public int getFuelWeight() {
            return fuelWeight;
        }

        public Config fuelWeight(int weight) {
            assert weight > 0;
            this.fuelWeight = weight;
            return this;
        }

        public int getTimeWeight() {
            return timeWeight;
        }

        public Config timeWeight(int weight) {
            assert weight > 0;
            this.timeWeight = weight;
            return this;
        }
    }
}

