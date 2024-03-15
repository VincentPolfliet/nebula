package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.nebula.automation.behaviour.tree.ShipBehaviourRefLeaf;
import dev.vinpol.nebula.automation.behaviour.tree.ShipSequenceBehaviour;
import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import dev.vinpol.torterra.IterableLeaf;
import dev.vinpol.torterra.Leaf;
import dev.vinpol.torterra.TorterraUtils;

import java.util.List;

public class DefaultBehaviourFactoryRegistry implements BehaviourFactoryRegistry {

    private final ApiClient apiClient;
    private final ShipEventNotifier eventNotifier;

    public DefaultBehaviourFactoryRegistry(ApiClient apiClient, ShipEventNotifier eventNotifier) {
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
}
