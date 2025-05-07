package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.function.Consumer;

@Data
public class OrbitShip200Response {
    private ShipNavModifiedResponseData data;

    public OrbitShip200Response data(ShipNavModifiedResponseData data) {
        this.data = data;
        return this;
    }
}
