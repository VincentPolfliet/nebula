package dev.vinpol.spacetraders.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.function.Consumer;

/**
 * The navigation information of the ship.
 */
@Data
public class ShipNav {
    private String systemSymbol;
    private String waypointSymbol;
    private ShipNavRoute route = new ShipNavRoute();
    private ShipNavStatus status;
    private ShipNavFlightMode flightMode = ShipNavFlightMode.CRUISE;

    public ShipNav systemSymbol(String systemSymbol) {
        this.systemSymbol = systemSymbol;
        return this;
    }

    public ShipNav waypointSymbol(String waypointSymbol) {
        this.waypointSymbol = waypointSymbol;
        return this;
    }

    public ShipNav route(ShipNavRoute route) {
        this.route = route;
        return this;
    }

    public ShipNav withRoute(Consumer<ShipNavRoute> consumer) {
        ShipNavRoute currentRoute = route != null ? route : new ShipNavRoute();
        consumer.accept(currentRoute);
        return route(currentRoute);
    }

    public ShipNav status(ShipNavStatus status) {
        this.status = status;
        return this;
    }

    public ShipNav flightMode(ShipNavFlightMode flightMode) {
        this.flightMode = flightMode;
        return this;
    }

    @JsonIgnore
    public boolean isInOrbit() {
        return getStatus() == ShipNavStatus.IN_ORBIT;
    }

    @JsonIgnore
    public boolean isInTransit() {
        return getStatus() == ShipNavStatus.IN_TRANSIT;
    }

    @JsonIgnore
    public boolean isDocked() {
        return getStatus() == ShipNavStatus.DOCKED;
    }

    public boolean isInSystem(String systemSymbol) {
        return getSystemSymbol().equals(systemSymbol);
    }
}

