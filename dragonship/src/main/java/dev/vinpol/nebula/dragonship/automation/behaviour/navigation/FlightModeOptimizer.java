package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.geo.GridXY;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class FlightModeOptimizer {

    private final Logger logger = LoggerFactory.getLogger(FlightModeOptimizer.class);

    private final FleetApi fleetApi;
    private final TravelCostCalculator travelCostCalculator;

    FlightModeOptimizer(FleetApi fleetApi, TravelCostCalculator travelCostCalculator) {
        this.fleetApi = fleetApi;
        this.travelCostCalculator = travelCostCalculator;
    }

    void optimizeFlightMode(Ship ship, SystemWaypoint currentLocation, SystemWaypoint targetLocation) {
        internalCalculate(ship, targetLocation.getSymbol(), GridXY.toCoordinate(currentLocation), GridXY.toCoordinate(targetLocation));
    }

    void optimizeFlightMode(Ship ship, Waypoint currentLocationWaypoint, Waypoint waypoint) {
        internalCalculate(ship, waypoint.getSymbol(), GridXY.toCoordinate(currentLocationWaypoint), GridXY.toCoordinate(waypoint));
    }

    private void internalCalculate(Ship ship, String symbol, GridXY origin, GridXY target) {
        Map<ShipNavFlightMode, Long> fuelCost = travelCostCalculator.calculateFuel(origin, target);

        if (fuelCost.isEmpty()) {
            throw new RuntimeException("fuel cost is not allowed to be null, that means that there is not way to navigate to the selected origin, has the current ship no fuel?");
        }

        ShipNavFlightMode bestFlightMode = travelCostCalculator.selectBestFlightMode(fuelCost);
        logger.debug("bestFlightMode for '{}': {}", ship.getSymbol(), bestFlightMode);

        if (fuelCost.containsKey(bestFlightMode)) {
            long bestFuelCostInUnits = fuelCost.get(bestFlightMode);
            double bestTimeInSeconds = travelCostCalculator.calculateTime(origin, target, ship.getEngine().getSpeed()).get(bestFlightMode);
            logger.info("Travelling to {} for {} will take +-{}s and will cost {} fuel units ({})", symbol, ship.getSymbol(), bestTimeInSeconds, bestFuelCostInUnits, bestFlightMode);
        }

        fleetApi.patchShipNav(ship.getSymbol(), new PatchShipNavRequest().flightMode(bestFlightMode));
    }
}
