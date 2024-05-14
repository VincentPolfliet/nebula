package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

/**
 * OrbitShip200ResponseData
 */

@Data
public class ShipNavModifiedResponseData {

    private ShipNav nav;

    public ShipNavModifiedResponseData nav(ShipNav nav) {
        this.nav = nav;
        return this;
    }
}

