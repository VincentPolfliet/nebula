package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.geo.GridXY;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class NavigateBehaviourFactory implements ShipBehaviourFactory {

    private final Logger logger = LoggerFactory.getLogger(NavigateBehaviourFactory.class);

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;
    private final TravelCostCalculator calculator;
    private final ShipEventNotifier eventNotifier;
    private final WaypointSymbol targetSymbol;

    public NavigateBehaviourFactory(FleetApi fleetApi, SystemsApi systemsApi, TravelCostCalculator calculator, ShipEventNotifier eventNotifier, WaypointSymbol targetSymbol) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.calculator = calculator;
        this.eventNotifier = eventNotifier;
        this.targetSymbol = targetSymbol;
    }

    @Override
    public ShipBehaviour create() {
        return ShipBehaviour.ofFunction(ship -> {
            WaypointSymbol currentLocationSymbol = WaypointSymbol.tryParse(ship.getNav().getWaypointSymbol());

            if (Objects.equals(currentLocationSymbol, targetSymbol)) {
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.ALREADY_AT_LOCATION));
            }

            if (ship.isFuelInfinite()) {
                return new NavigationShipBehaviour(fleetApi, systemsApi, new FlightModeOptimizer(fleetApi, calculator), eventNotifier, targetSymbol);
            }

            Waypoint currentLocation = systemsApi.getWaypoint(currentLocationSymbol.system(), currentLocationSymbol.waypoint()).getData();
            Waypoint targetLocation = systemsApi.getWaypoint(targetSymbol.system(), targetSymbol.waypoint()).getData();


            ShipNavFlightMode possibleMode = getPossibleModeExcludingDrift(ship, currentLocation, targetLocation);

            if (possibleMode != null && possibleMode != ShipNavFlightMode.DRIFT) {
                return new NavigationShipBehaviour(fleetApi, systemsApi, new FlightModeOptimizer(fleetApi, calculator), eventNotifier, targetSymbol);
            }

            // complex behaviour with refueling
            return new NavigationUsingGraphBehaviourFactory(fleetApi, eventNotifier, systemsApi, calculator, targetSymbol).create();
        });
    }

    private ShipNavFlightMode getPossibleModeExcludingDrift(Ship ship, Waypoint currentLocation, Waypoint targetLocation) {
        Map<ShipNavFlightMode, Long> cost = calculator.calculateFuel(GridXY.toCoordinate(currentLocation), GridXY.toCoordinate(targetLocation));

        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            if (mode != ShipNavFlightMode.DRIFT) {
                Long routeFuelCost = cost.get(mode);

                if (routeFuelCost - ship.getFuel().getCurrent() > 0) {
                    return mode;
                }
            }
        }

        return null;
    }
}
