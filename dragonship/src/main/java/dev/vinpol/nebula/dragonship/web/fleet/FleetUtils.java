package dev.vinpol.nebula.dragonship.web.fleet;

import dev.vinpol.spacetraders.sdk.models.Ship;

public class FleetUtils {

    private FleetUtils() {

    }

    public static double calculateAverageIntegrity(Ship ship) {
        double reactorIntegrity = ship.getReactor().getIntegrity();
        double frameIntegrity = ship.getFrame().getIntegrity();
        double engineIntegrity = ship.getEngine().getIntegrity();

        return ((reactorIntegrity + frameIntegrity + engineIntegrity) / 3) * 100;
    }

    public static double calculateAverageCondition(Ship ship) {
        double reactorCondition = ship.getReactor().getCondition();
        double frameCondition = ship.getFrame().getCondition();
        double engineCondition = ship.getEngine().getCondition();

        return ((reactorCondition + frameCondition + engineCondition) / 3) * 100;
    }

    public static String formatCondition(double value) {
        return String.format("%5.2f", value);
    }

    public static String formatDistance(double value) {
        return String.format("%d", Math.round(value));
    }
}
