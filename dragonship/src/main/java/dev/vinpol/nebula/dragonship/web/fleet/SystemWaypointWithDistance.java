package dev.vinpol.nebula.dragonship.web.fleet;

import dev.vinpol.spacetraders.sdk.models.SystemWaypoint;
import dev.vinpol.spacetraders.sdk.models.WaypointType;

public record SystemWaypointWithDistance(SystemWaypoint waypoint, double distance) {
    public String getSymbol() {
        return waypoint.getSymbol();
    }

    public WaypointType getType() {
        return waypoint.getType();
    }
}
