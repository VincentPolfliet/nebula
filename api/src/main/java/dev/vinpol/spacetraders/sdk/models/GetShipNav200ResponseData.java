package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.List;

@Data
public class GetShipNav200ResponseData {
    private ShipNav nav;
    private ShipFuel fuel;
    private List<ShipEvent> events;

    public GetShipNav200ResponseData fuel(ShipFuel fuel) {
        this.fuel = fuel;
        return this;
    }

    public GetShipNav200ResponseData nav(ShipNav nav) {
        this.nav = nav;
        return this;
    }
}

