package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

/**
 * The public registration information of the ship
 */

@Data
public class ShipRegistration {
    private String name;
    private String factionSymbol;
    private ShipRole role;

    public ShipRegistration name(String name) {
        this.name = name;
        return this;
    }

    public ShipRegistration factionSymbol(String factionSymbol) {
        this.factionSymbol = factionSymbol;
        return this;
    }

    public ShipRegistration role(ShipRole role) {
        this.role = role;
        return this;
    }
}

