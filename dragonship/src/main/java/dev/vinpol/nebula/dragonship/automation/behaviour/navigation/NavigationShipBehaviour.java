package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

class NavigationShipBehaviour implements ShipBehaviour {

    private final Logger logger = LoggerFactory.getLogger(NavigationShipBehaviour.class);

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;

    private final TravelFuelAndTimerCalculator travelFuelAndTimerCalculator;
    private final ShipEventNotifier shipEventNotifier;

    private final WaypointSymbol waypointSymbol;

    NavigationShipBehaviour(FleetApi fleetApi, SystemsApi systemsApi, TravelFuelAndTimerCalculator travelFuelAndTimerCalculator, ShipEventNotifier shipEventNotifier, WaypointSymbol waypointSymbol) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.travelFuelAndTimerCalculator = travelFuelAndTimerCalculator;
        this.shipEventNotifier = shipEventNotifier;
        this.waypointSymbol = waypointSymbol;
    }

    @Override
    public String getName() {
        return "navigate";
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        if (ship.isFuelEmpty()) {
            logger.trace("fuel is empty");
            return ShipBehaviourResult.failure(FailureReason.FUEL_IS_EMPTY);
        }

        if (ship.isNotInOrbit()) {
            logger.trace("ship is not in orbit");
            return ShipBehaviourResult.failure(FailureReason.NOT_IN_ORBIT);
        }

        String waypointSymbolString = waypointSymbol.waypoint();
        if (ship.isAtLocation(waypointSymbolString)) {
            logger.trace("ship is already at location");
            return ShipBehaviourResult.failure(FailureReason.ALREADY_AT_LOCATION);
        }

        WaypointSymbol currentLocationWaypointSymbol = WaypointSymbol.tryParse(ship.getNav().getWaypointSymbol());

        Waypoint currentLocationWaypoint = systemsApi.getWaypoint(currentLocationWaypointSymbol.system(), currentLocationWaypointSymbol.waypoint()).getData();
        Waypoint waypoint = systemsApi.getWaypoint(waypointSymbol.system(), waypointSymbolString).getData();

        optimizeFlightMode(ship, currentLocationWaypoint, waypoint);

        logger.info("Navigating '{}' to travel from '{}' to '{}' ", ship.getSymbol(), currentLocationWaypoint.getSymbol(), waypoint.getSymbol());

        NavigateShip200ResponseData navigationResponse = fleetApi.navigateShip(
            ship.getSymbol(),
            new NavigateShipRequest()
                .waypointSymbol(waypointSymbolString)
        ).getData();

        ship.setFuel(navigationResponse.getFuel());

        // TODO: get consider value from config
        if (ship.isFuelEmpty() || ship.considerFuelEmpty(0.2)) {
            shipEventNotifier.setFuelIsAlmostEmpty(ship.getSymbol());
        }

        ship.setNav(navigationResponse.getNav());

        OffsetDateTime arrival = ship.getNav().getRoute().getArrival();
        shipEventNotifier.setWaitUntilArrival(ship.getSymbol(), arrival);
        return ShipBehaviourResult.waitUntil(arrival);
    }

    private void optimizeFlightMode(Ship ship, Waypoint currentLocationWaypoint, Waypoint waypoint) {
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
