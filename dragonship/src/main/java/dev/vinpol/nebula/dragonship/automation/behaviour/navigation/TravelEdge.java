package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import dev.vinpol.spacetraders.sdk.models.SystemWaypoint;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.Map;

public record TravelEdge(
    Waypoint origin,
    Waypoint target,
    Map<ShipNavFlightMode, Long> fuelCost,
    Map<ShipNavFlightMode, Double> timeCost,
    double distance) {


    public static long calculateFuel(GraphPath<?, TravelEdge> directPath, ShipNavFlightMode mode) {
        return calculateFuel(directPath.getEdgeList(), mode);
    }

    public static long calculateFuel(List<TravelEdge> edgeList, ShipNavFlightMode mode) {
        return edgeList.stream().mapToLong(e -> e.fuelCost().get(mode)).sum();
    }

    @Override
    public String toString() {
        return "TravelEdge{fuel: " + fuelCost + ", time: " + timeCost + ", distance: " + distance + '}';
    }
}
