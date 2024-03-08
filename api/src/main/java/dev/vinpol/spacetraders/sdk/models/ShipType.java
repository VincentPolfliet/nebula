package dev.vinpol.spacetraders.sdk.models;

import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Type of ship
 */

public enum ShipType {

    PROBE("SHIP_PROBE"),

    MINING_DRONE("SHIP_MINING_DRONE"),

    SIPHON_DRONE("SHIP_SIPHON_DRONE"),

    INTERCEPTOR("SHIP_INTERCEPTOR"),

    LIGHT_HAULER("SHIP_LIGHT_HAULER"),

    COMMAND_FRIGATE("SHIP_COMMAND_FRIGATE"),

    EXPLORER("SHIP_EXPLORER"),

    HEAVY_FREIGHTER("SHIP_HEAVY_FREIGHTER"),

    LIGHT_SHUTTLE("SHIP_LIGHT_SHUTTLE"),

    ORE_HOUND("SHIP_ORE_HOUND"),

    REFINING_FREIGHTER("SHIP_REFINING_FREIGHTER"),

    SURVEYOR("SHIP_SURVEYOR");

    private String value;

    ShipType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ShipType fromValue(String value) {
        for (ShipType b : ShipType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

