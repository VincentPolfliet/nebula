package dev.vinpol.spacetraders.sdk.models;

/**
 * The current navigation status of the ship
 */
public enum ShipNavStatus {

    IN_TRANSIT,
    IN_ORBIT,
    DOCKED;

    public static ShipNavStatus ofName(String name) {
        for (ShipNavStatus b : ShipNavStatus.values()) {
            if (b.name().equals(name)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + name + "'");
    }
}

