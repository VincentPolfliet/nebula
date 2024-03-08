package dev.vinpol.spacetraders.sdk.models;

import lombok.Getter;

/**
 * The registered role of the ship
 */

@Getter
public enum ShipRole {

    FABRICATOR("FABRICATOR"),

    HARVESTER("HARVESTER"),

    HAULER("HAULER"),

    INTERCEPTOR("INTERCEPTOR"),

    EXCAVATOR("EXCAVATOR"),

    TRANSPORT("TRANSPORT"),

    REPAIR("REPAIR"),

    SURVEYOR("SURVEYOR"),

    COMMAND("COMMAND"),

    CARRIER("CARRIER"),

    PATROL("PATROL"),

    SATELLITE("SATELLITE"),

    EXPLORER("EXPLORER"),

    REFINERY("REFINERY");

    private final String value;

    ShipRole(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ShipRole fromValue(String value) {
        for (ShipRole b : ShipRole.values()) {
            if (b.getValue().equals(value)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

