package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

@Data
public class NavigateShipRequest {
    private String waypointSymbol;

    public NavigateShipRequest waypointSymbol(String waypointSymbol) {
        this.waypointSymbol = waypointSymbol;
        return this;
    }
}

