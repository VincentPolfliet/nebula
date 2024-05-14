package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.function.Consumer;

@Data
public class DockShip200Response implements Consumer<Ship> {
    private ShipNavModifiedResponseData data;

    public DockShip200Response data(ShipNavModifiedResponseData data) {
        this.data = data;
        return this;
    }

    @Override
    public void accept(Ship ship) {
        ship.setNav(data.getNav());
    }
}

