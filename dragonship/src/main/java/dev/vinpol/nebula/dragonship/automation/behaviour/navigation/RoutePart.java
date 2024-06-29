package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import dev.vinpol.spacetraders.sdk.models.Waypoint;

public record RoutePart(Waypoint origin, Waypoint target, ShipNavFlightMode mode, TravelEdge edge) {
    public long fuelCost() {
        return edge.fuelCost().get(mode);
    }

    public double timeCost() {
        return edge.timeCost().get(mode);
    }

    public double distance() {
        return edge.distance();
    }
}
