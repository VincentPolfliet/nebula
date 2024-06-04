package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigateBehaviourFactory implements ShipBehaviourFactory {

    private final Logger logger = LoggerFactory.getLogger(NavigateBehaviourFactory.class);

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;
    private final FlightModeOptimizer flightModeOptimizer;
    private final ShipEventNotifier shipEventNotifier;
    private final WaypointSymbol waypointSymbol;

    public NavigateBehaviourFactory(FleetApi fleetApi, SystemsApi systemsApi, TravelFuelAndTimerCalculator flightModeOptimizer, ShipEventNotifier shipEventNotifier, WaypointSymbol waypointSymbol) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.flightModeOptimizer = new FlightModeOptimizer(fleetApi, flightModeOptimizer);
        this.shipEventNotifier = shipEventNotifier;
        this.waypointSymbol = waypointSymbol;
    }

    @Override
    public ShipBehaviour create() {
        return new NavigationShipBehaviour(fleetApi, systemsApi, flightModeOptimizer, shipEventNotifier, waypointSymbol);
    }
}
