package dev.vinpol.nebula.dragonship.web.fleet;

import dev.vinpol.spacetraders.sdk.models.Ship;

public class FleetUtils {

    private FleetUtils() {

    }

    public static double calculateAverageIntegrity(Ship ship) {
        int reactorIntegrity = ship.getReactor().getIntegrity();
        int frameIntegrity = ship.getFrame().getIntegrity();
        int engineIntegrity = ship.getEngine().getIntegrity();

        return (((double) reactorIntegrity + frameIntegrity + engineIntegrity) / 3) * 100;
    }

    public static double calculateAverageCondition(Ship ship) {
        int reactorCondition = ship.getReactor().getCondition();
        int frameCondition = ship.getFrame().getCondition();
        int engineCondition = ship.getEngine().getCondition();

        return (((double) reactorCondition + frameCondition + engineCondition) / 3) * 100;
    }
}
