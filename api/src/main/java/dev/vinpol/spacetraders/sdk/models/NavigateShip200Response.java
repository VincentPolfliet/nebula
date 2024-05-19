package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

@Data
public class NavigateShip200Response {

    private NavigateShip200ResponseData data;

    public NavigateShip200Response data(NavigateShip200ResponseData data) {
        this.data = data;
        return this;
    }
}

