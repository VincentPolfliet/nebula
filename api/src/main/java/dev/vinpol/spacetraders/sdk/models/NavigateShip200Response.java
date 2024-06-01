package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.function.Consumer;

@Data
public class NavigateShip200Response implements Consumer<Ship> {

    private NavigateShip200ResponseData data;

    public NavigateShip200Response data(NavigateShip200ResponseData data) {
        this.data = data;
        return this;
    }

    @Override
    public void accept(Ship ship) {
        ship.setNav(data.getNav());
        ship.setFuel(data.getFuel());
    }
}

