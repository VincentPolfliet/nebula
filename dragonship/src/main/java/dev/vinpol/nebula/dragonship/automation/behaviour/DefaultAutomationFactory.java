package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.market.FindMarketAndSellBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.navigation.NavigateBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.sell.SellCargoBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.models.TradeSymbol;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import org.springframework.stereotype.Component;

@Component
public class DefaultAutomationFactory implements AutomationFactory {

    private final ApiClient apiClient;
    private final TravelFuelAndTimerCalculator travelFuelAndTimerCalculator;
    private final ShipEventNotifier eventNotifier;

    public DefaultAutomationFactory(ApiClient apiClient, TravelFuelAndTimerCalculator travelFuelAndTimerCalculator, ShipEventNotifier eventNotifier) {
        this.apiClient = apiClient;
        this.travelFuelAndTimerCalculator = travelFuelAndTimerCalculator;
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
        return new NavigateBehaviourFactory(apiClient.fleetApi(), apiClient.systemsApi(), travelFuelAndTimerCalculator, eventNotifier, waypointSymbol);
    }

    @Override
    public OrbitBehaviourFactory orbitAutomation() {
        return new OrbitBehaviourFactory(apiClient.fleetApi());
    }

    @Override
    public DockBehaviourFactory dock() {
        return new DockBehaviourFactory(apiClient.fleetApi());
    }

    @Override
    public RefuelBehaviourFactory refuel() {
        return new RefuelBehaviourFactory(apiClient.fleetApi(), apiClient.systemsApi());
    }

    @Override
    public FindMarketAndSellBehaviourFactory navigateToClosestMarket() {
        return new FindMarketAndSellBehaviourFactory(this, apiClient.systemsApi(), travelFuelAndTimerCalculator);
    }

    @Override
    public SellCargoBehaviourFactory sellCargo(TradeSymbol tradeSymbol, int units) {
        return new SellCargoBehaviourFactory(apiClient.fleetApi(), tradeSymbol, units);
    }
}
