package dev.vinpol.nebula.dragonship.geo;

import dev.vinpol.spacetraders.sdk.models.ShipNavRouteWaypoint;
import dev.vinpol.spacetraders.sdk.models.Waypoint;

public record Coordinate(double x, double y) {

    public static Coordinate toCoordinate(Waypoint targetWaypoint) {
        return new Coordinate(
            targetWaypoint.getX(),
            targetWaypoint.getY()
        );
    }

    public static Coordinate toCoordinate(ShipNavRouteWaypoint waypoint) {
        return new Coordinate(
            waypoint.getX(),
            waypoint.getY()
        );
    }
}
