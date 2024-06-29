package dev.vinpol.nebula.dragonship.geo;

import dev.vinpol.spacetraders.sdk.models.ShipNavRouteWaypoint;
import dev.vinpol.spacetraders.sdk.models.SystemWaypoint;
import dev.vinpol.spacetraders.sdk.models.Waypoint;

public record GridXY(double x, double y) {

    public static GridXY toCoordinate(SystemWaypoint targetWaypoint) {
        return new GridXY(
            targetWaypoint.getX(),
            targetWaypoint.getY()
        );
    }

    public static GridXY toCoordinate(Waypoint targetWaypoint) {
        return new GridXY(
            targetWaypoint.getX(),
            targetWaypoint.getY()
        );
    }

    public static GridXY toCoordinate(ShipNavRouteWaypoint waypoint) {
        return new GridXY(
            waypoint.getX(),
            waypoint.getY()
        );
    }
}
