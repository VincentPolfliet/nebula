package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.List;

@Data
public class NavigateShip200ResponseData {
    private ShipFuel fuel;
    private ShipNav nav;
    private List<ShipEvent> events;

    public NavigateShip200ResponseData fuel(ShipFuel fuel) {
        this.fuel = fuel;
        return this;
    }

    public NavigateShip200ResponseData nav(ShipNav nav) {
        this.nav = nav;
        return this;
    }
}

