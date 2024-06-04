package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class NavigateBehaviourFactory implements ShipBehaviourFactory {

    private final Logger logger = LoggerFactory.getLogger(NavigateBehaviourFactory.class);

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;
    private final TravelFuelAndTimerCalculator travelFuelAndTimerCalculator;
    private final ShipEventNotifier shipEventNotifier;
    private final WaypointSymbol waypointSymbol;

    public NavigateBehaviourFactory(FleetApi fleetApi, SystemsApi systemsApi, TravelFuelAndTimerCalculator travelFuelAndTimerCalculator, ShipEventNotifier shipEventNotifier, WaypointSymbol waypointSymbol) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.travelFuelAndTimerCalculator = travelFuelAndTimerCalculator;
        this.shipEventNotifier = shipEventNotifier;
        this.waypointSymbol = waypointSymbol;
    }

    @Override
    public ShipBehaviour create() {
        return new NavigationShipBehaviour(fleetApi, systemsApi, travelFuelAndTimerCalculator, shipEventNotifier, waypointSymbol);
    }
}
