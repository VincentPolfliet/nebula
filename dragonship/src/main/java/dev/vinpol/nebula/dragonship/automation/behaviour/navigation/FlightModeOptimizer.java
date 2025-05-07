package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.geo.GridXY;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
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
        Map<ShipNavFlightMode, Duration> durationCost = travelCostCalculator.calculateTime(origin, target, ship.getEngine().getSpeed());

        ShipNavFlightMode bestFlightModeByDuration = travelCostCalculator.selectBestFlightModeByDuration(durationCost);
        logger.debug("bestFlightModeDuration for '{}': {}", ship.getSymbol(), bestFlightModeByDuration);


        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>(travelCostCalculator.calculateFuel(origin, target));

        for (Iterator<Map.Entry<ShipNavFlightMode, Long>> it = fuelCost.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<ShipNavFlightMode, Long> entry = it.next();
            Long cost = entry.getValue();

            if (ship.getFuel().getCurrent() - cost < 0) {
                it.remove();
            }
        }

        if (fuelCost.isEmpty()) {
            throw new RuntimeException("fuel cost is not allowed to be null, that means that there is not way to navigate to the selected origin, has the current ship no fuel?");
        }

        ShipNavFlightMode bestFlightModeByFuel = travelCostCalculator.selectBestFlightModeByFuel(fuelCost);
        logger.debug("bestFlightModeFuel for '{}': {}", ship.getSymbol(), bestFlightModeByFuel);

        ShipNavFlightMode flightModeOptimal = fuelCost.containsKey(bestFlightModeByDuration) || bestFlightModeByDuration == bestFlightModeByFuel ? bestFlightModeByDuration : bestFlightModeByFuel;
        Duration bestDurationCostInSeconds = durationCost.get(flightModeOptimal);
        double bestFuelCostInUnits = fuelCost.get(flightModeOptimal);
        logger.info("Travelling to {} for {} will take +-{}s and will cost {} fuel units ({})", symbol, ship.getSymbol(), bestDurationCostInSeconds.toSeconds(), bestFuelCostInUnits, flightModeOptimal);

        fleetApi.patchShipNav(ship.getSymbol(), new PatchShipNavRequest().flightMode(flightModeOptimal));
    }
}
