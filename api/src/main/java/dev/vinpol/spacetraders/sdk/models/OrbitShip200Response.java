package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.function.Consumer;

@Data
public class OrbitShip200Response implements Consumer<Ship> {
    private ShipNavModifiedResponseData data;

    public OrbitShip200Response data(ShipNavModifiedResponseData data) {
        this.data = data;
        return this;
    }

    @Override
    public void accept(Ship ship) {
        ship.setNav(data.getNav());
    }
}

