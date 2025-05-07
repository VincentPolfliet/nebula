package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.function.Consumer;

@Data
public class DockShip200Response {
    private ShipNavModifiedResponseData data;

    public DockShip200Response data(ShipNavModifiedResponseData data) {
        this.data = data;
        return this;
    }
}

