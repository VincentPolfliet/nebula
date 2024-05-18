package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.function.Consumer;

@Data
public class GetShipNav200Response implements Consumer<Ship> {
    private ShipNav data;

    public GetShipNav200Response data(ShipNav data) {
        this.data = data;
        return this;
    }

    @Override
    public void accept(Ship ship) {
        ship.setNav(getData());
    }
}

