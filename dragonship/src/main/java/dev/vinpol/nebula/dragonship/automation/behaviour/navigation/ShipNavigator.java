package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.spacetraders.sdk.models.ShipFuel;
import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dev.vinpol.nebula.dragonship.automation.behaviour.navigation.TravelEdge.calculateFuel;


public class ShipNavigator {

    private final Logger logger = LoggerFactory.getLogger(ShipNavigator.class);

    private final Graph<String, TravelEdge> graph;
    private final Map<String, Waypoint> waypoints;

    public ShipNavigator(Graph<String, TravelEdge> graph, Collection<Waypoint> waypoints) {
        this.graph = graph;
        this.waypoints = waypoints.stream().collect(Collectors.toMap(Waypoint::getSymbol, Function.identity()));
    }

    public Route findPath(ShipFuel fuel, Waypoint origin, Waypoint target) {
        ShipNavFlightMode bestMode = null;
        GraphPath<String, TravelEdge> bestPath = null;

        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            Graph<String, TravelEdge> currentGraph = new AsWeightedGraph<>(graph, edge -> calculateWeight(mode, edge), false, false);

            GraphPath<String, TravelEdge> directPath = new DijkstraShortestPath<>(currentGraph).getPath(origin.getSymbol(), target.getSymbol());

            logger.debug("directPath for '{}': {}", mode, directPath);

            if (directPath == null) {
                continue;
            }

            long fuelCostOfEdges = calculateFuel(directPath.getEdgeList(), mode);
            boolean isPossibleByFuel = fuel.isInfinite() || fuel.getCurrent() - fuelCostOfEdges > 0;

            if (isPossibleByFuel && directPath.getLength() != 0) {
                bestMode = mode;
                bestPath = directPath;
            }
        }

        logger.info("bestMode for '{}' -> '{}': {}", origin.getSymbol(), target.getSymbol(), bestMode);

        if (bestMode == null) {
            return null;
        }

        return convertToRoute(bestPath, bestMode);
    }

    private Double calculateWeight(ShipNavFlightMode mode, TravelEdge travelEdge) {
        return travelEdge.timeCost().getOrDefault(mode, 0d);
    }

    private Route convertToRoute(GraphPath<String, TravelEdge> path, ShipNavFlightMode mode) {
        List<RoutePart> parts = new ArrayList<>();

        List<String> vertexList = path.getVertexList();
        for (int i = 0, vertexListSize = vertexList.size(); i < vertexListSize - 1; i++) {
            var last = vertexList.get(i);
            var next = vertexList.get(i + 1);
            var edge = path.getEdgeList().get(i);

            parts.add(new RoutePart(getWaypoint(last), getWaypoint(next), mode, edge));
        }

        return new Route(parts);
    }

    private Waypoint getWaypoint(String inSymbol) {
        return waypoints.getOrDefault(inSymbol, null);
    }
}
