package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.function.Consumer;

/**
 * The navigation information of the ship.
 */
@Data
public class ShipNav {
    private String systemSymbol;
    private String waypointSymbol;
    private ShipNavRoute route;
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
        ShipNavRoute route = new ShipNavRoute();
        consumer.accept(route);
        return route(route);
    }

    public ShipNav status(ShipNavStatus status) {
        this.status = status;
        return this;
    }

    public ShipNav flightMode(ShipNavFlightMode flightMode) {
        this.flightMode = flightMode;
        return this;
    }

    public boolean isInOrbit() {
        return getStatus() == ShipNavStatus.IN_ORBIT;
    }

    public boolean isInTransit() {
        return getStatus() == ShipNavStatus.IN_TRANSIT;
    }

    public boolean isDocked() {
        return getStatus() == ShipNavStatus.DOCKED;
    }
}

