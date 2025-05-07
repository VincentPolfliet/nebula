package dev.vinpol.nebula.dragonship.automation.behaviour;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.automation.behaviour.docking.DockShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.market.FindMarketAndSellBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.navigation.NavigateBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.sell.SellCargoBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.models.TradeSymbol;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import org.springframework.stereotype.Component;

@Component
public class DefaultAutomationFactory implements AutomationFactory {

    private final ApiClient apiClient;
    private final TravelCostCalculator travelCostCalculator;
    private final ShipEventNotifier eventNotifier;

    public DefaultAutomationFactory(ApiClient apiClient, TravelCostCalculator travelCostCalculator, ShipEventNotifier eventNotifier) {
        this.apiClient = apiClient;
        this.travelCostCalculator = travelCostCalculator;
        this.eventNotifier = eventNotifier;
    }

    @Override
    public MiningBehaviourFactory miningAutomation(SystemSymbol system, WaypointType waypointType) {
        return new MiningBehaviourFactory(apiClient.systemsApi(), this, system, waypointType);
    }

    @Override
    public ExtractionBehaviourFactory extraction() {
        return new ExtractionBehaviourFactory(apiClient.fleetApi(), eventNotifier);
    }

    @Override
    public NavigateBehaviourFactory navigateAutomation(WaypointSymbol waypointSymbol) {
        return new NavigateBehaviourFactory(apiClient.fleetApi(), apiClient.systemsApi(), travelCostCalculator, eventNotifier, waypointSymbol);
    }

    @Override
    public OrbitBehaviourFactory orbitAutomation() {
        return new OrbitBehaviourFactory(apiClient.fleetApi(), eventNotifier);
    }

    @Override
    public ShipBehaviourFactory dock() {
        return () -> new DockShipBehaviour(apiClient.fleetApi(), eventNotifier);
    }

    @Override
    public RefuelBehaviourFactory refuel() {
        return new RefuelBehaviourFactory(apiClient.fleetApi(), apiClient.systemsApi());
    }

    @Override
    public FindMarketAndSellBehaviourFactory navigateToClosestMarket() {
        return new FindMarketAndSellBehaviourFactory(this, apiClient.systemsApi(), travelCostCalculator);
    }

    @Override
    public SellCargoBehaviourFactory sellCargo(TradeSymbol tradeSymbol, int units) {
        return new SellCargoBehaviourFactory(apiClient.fleetApi(), tradeSymbol, units);
    }
}
