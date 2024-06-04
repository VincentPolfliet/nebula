package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.PatchShipNavRequest;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class FlightModeOptimizer {

    private final Logger logger = LoggerFactory.getLogger(FlightModeOptimizer.class);

    private final FleetApi fleetApi;
    private final TravelFuelAndTimerCalculator travelFuelAndTimerCalculator;

    FlightModeOptimizer(FleetApi fleetApi, TravelFuelAndTimerCalculator travelFuelAndTimerCalculator) {
        this.fleetApi = fleetApi;
        this.travelFuelAndTimerCalculator = travelFuelAndTimerCalculator;
    }

    void optimizeFlightMode(Ship ship, Waypoint currentLocationWaypoint, Waypoint waypoint) {
        Coordinate origin = Coordinate.toCoordinate(currentLocationWaypoint);
        Coordinate target = Coordinate.toCoordinate(waypoint);
        int currentFuel = ship.getFuel().getCurrent();

        Map<ShipNavFlightMode, Long> fuelCost = travelFuelAndTimerCalculator.calculateFuel(origin, target);
        Map<ShipNavFlightMode, Double> timeCost = travelFuelAndTimerCalculator.calculateTime(origin, target, ship.getEngine().getSpeed());

        for (ShipNavFlightMode flightMode : ShipNavFlightMode.values()) {
            Long costOfFlightMode = fuelCost.getOrDefault(flightMode, 1L);

            if (currentFuel - costOfFlightMode <= 0) {
                fuelCost.remove(flightMode);
                timeCost.remove(flightMode);
            }
        }

        if (fuelCost.isEmpty()) {
            throw new RuntimeException("fuel cost is not allowed to be null, that means that there is not way to navigate to the selected waypoint, has the current ship no fuel?");
        }

        ShipNavFlightMode bestFlightMode = travelFuelAndTimerCalculator.selectBestFlightMode(timeCost, fuelCost);
        logger.debug("bestFlightMode for '{}': {}", ship.getSymbol(), bestFlightMode);

        if (fuelCost.containsKey(bestFlightMode) && timeCost.containsKey(bestFlightMode)) {
            long bestFuelCostInUnits = fuelCost.get(bestFlightMode);
            double bestTimeInSeconds = timeCost.get(bestFlightMode);

            logger.info("Travelling to {} for {} will take +-{}s and will cost {} fuel units ({})", waypoint.getSymbol(), ship.getSymbol(), bestTimeInSeconds, bestFuelCostInUnits, bestFlightMode);
        }

        fleetApi.patchShipNav(ship.getSymbol(), new PatchShipNavRequest().flightMode(bestFlightMode));
    }
}
