package dev.vinpol.spacetraders.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
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
        ShipNavRouteWaypoint dest = this.destination != null ? this.destination : new ShipNavRouteWaypoint();
        consumer.accept(dest);
        return destination(dest);
    }

    @JsonIgnore
    public long getDurationInSeconds() {
        return ChronoUnit.SECONDS.between(getArrival(), getDepartureTime());
    }
}

