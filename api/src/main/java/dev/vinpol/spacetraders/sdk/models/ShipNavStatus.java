package dev.vinpol.spacetraders.sdk.models;

import lombok.Getter;

/**
 * The current navigation status of the ship
 */
@Getter
public enum ShipNavStatus {

    IN_TRANSIT("IN_TRANSIT"),

    IN_ORBIT("IN_ORBIT"),

    DOCKED("DOCKED");

    private final String value;

    ShipNavStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ShipNavStatus fromValue(String value) {
        for (ShipNavStatus b : ShipNavStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

