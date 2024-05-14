package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import org.springframework.stereotype.Component;

@Component
public class DefaultShipBehaviourFactoryCreator implements ShipBehaviourFactoryCreator {

    private final ApiClient apiClient;
    private final ShipEventNotifier eventNotifier;

    public DefaultShipBehaviourFactoryCreator(ApiClient apiClient, ShipEventNotifier eventNotifier) {
        this.apiClient = apiClient;
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
        return new NavigateBehaviourFactory(apiClient.fleetApi(), eventNotifier, waypointSymbol);
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
    public RefuelBehaviour refuel() {
        return new RefuelBehaviour(apiClient.fleetApi());
    }

    @Override
    public FindMarketAndSellBehaviour navigateToClosestMarket() {
        return new FindMarketAndSellBehaviour(this, apiClient.systemsApi());
    }
}
