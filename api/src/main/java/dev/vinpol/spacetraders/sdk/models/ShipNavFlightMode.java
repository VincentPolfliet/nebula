package dev.vinpol.spacetraders.sdk.models;

/**
 * The ship&#39;s set speed when traveling between waypoints or systems.
 */
public enum ShipNavFlightMode {
    DRIFT,
    STEALTH,
    CRUISE,
    BURN;

    public static ShipNavFlightMode fromName(String value) {
        for (ShipNavFlightMode b : ShipNavFlightMode.values()) {
            if (b.name().equals(value)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

