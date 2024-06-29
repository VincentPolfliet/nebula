package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import dev.vinpol.spacetraders.sdk.models.Waypoint;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public record Route(Collection<RoutePart> routes) {

    public boolean isAllDrift() {
        return routes.stream()
            .allMatch(r -> r.mode() == ShipNavFlightMode.DRIFT);
    }

    public long fuelCost() {
        return routes.stream()
            .mapToLong(RoutePart::fuelCost)
            .sum();
    }

    public double distance() {
        return routes.stream()
            .mapToDouble(RoutePart::distance)
            .sum();
    }

    public boolean isEmpty() {
        return routes.isEmpty();
    }

    public List<Waypoint> getVertexList() {
        return routes.stream()
            .flatMap(r -> Stream.of(r.origin(), r.target()))
            .distinct()
            .toList();
    }

    public List<TravelEdge> getEdgeList() {
        return routes.stream()
            .map(RoutePart::edge)
            .toList();
    }
}
