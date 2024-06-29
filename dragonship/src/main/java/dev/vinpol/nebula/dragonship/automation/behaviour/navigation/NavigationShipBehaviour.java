package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

class NavigationShipBehaviour implements ShipBehaviour {

    private final Logger logger = LoggerFactory.getLogger(NavigationShipBehaviour.class);

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;

    private final FlightModeOptimizer flightModeOptimizer;
    private final ShipEventNotifier shipEventNotifier;

    private final WaypointSymbol waypointSymbol;

    NavigationShipBehaviour(FleetApi fleetApi, SystemsApi systemsApi, FlightModeOptimizer flightModeOptimizer, ShipEventNotifier shipEventNotifier, WaypointSymbol waypointSymbol) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.flightModeOptimizer = flightModeOptimizer;
        this.shipEventNotifier = shipEventNotifier;
        this.waypointSymbol = waypointSymbol;
    }

    @Override
    public String getName() {
        return "navigate";
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        if (ship.isFuelEmpty() && ship.getFuel().isNotInfinite()) {
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

        if (ship.isFuelNotInfinite()) {
            flightModeOptimizer.optimizeFlightMode(ship, currentLocationWaypoint, waypoint);
        }

        logger.info("Navigating '{}' to travel from '{}' to '{}' ", ship.getSymbol(), currentLocationWaypoint.getSymbol(), waypoint.getSymbol());

        NavigateShip200ResponseData navigationResponse = fleetApi.navigateShip(
            ship.getSymbol(),
            new NavigateShipRequest()
                .waypointSymbol(waypointSymbolString)
        ).getData();

        ship.setFuel(navigationResponse.getFuel());
        ship.setNav(navigationResponse.getNav());

        if (ship.isFuelEmpty() || ship.considerFuelEmpty(0.2)) {
            shipEventNotifier.setFuelIsAlmostEmpty(ship.getSymbol());
        }

        ShipNavRoute currentRoute = ship.getNav().getRoute();

        OffsetDateTime arrival = currentRoute.getArrival();
        shipEventNotifier.setNavigatingTo(ship.getSymbol(), waypointSymbol, currentRoute.getArrival());
        return ShipBehaviourResult.waitUntil(arrival);
    }
}
