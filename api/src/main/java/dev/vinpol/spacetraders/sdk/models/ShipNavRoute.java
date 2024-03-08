package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.function.Consumer;

/**
 * The routing information for the ship&#39;s most recent transit or current location.
 */
@Data
public class ShipNavRoute {
    private ShipNavRouteWaypoint destination;
    private ShipNavRouteWaypoint origin;
    private OffsetDateTime departureTime;
    private OffsetDateTime arrival;

    public ShipNavRoute destination(ShipNavRouteWaypoint destination) {
        this.destination = destination;
        return this;
    }

    public ShipNavRoute origin(ShipNavRouteWaypoint origin) {

        this.origin = origin;
        return this;
    }

    public ShipNavRoute departureTime(OffsetDateTime departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public ShipNavRoute arrival(OffsetDateTime arrival) {
        this.arrival = arrival;
        return this;
    }

    public ShipNavRoute withDestination(Consumer<ShipNavRouteWaypoint> consumer) {
        ShipNavRouteWaypoint waypoint = new ShipNavRouteWaypoint();
        consumer.accept(waypoint);
        return this;
    }
}

