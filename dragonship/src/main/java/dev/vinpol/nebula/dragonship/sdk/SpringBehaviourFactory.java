package dev.vinpol.nebula.dragonship.sdk;

import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.nebula.automation.behaviour.*;
import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringBehaviourFactory implements BehaviourFactoryRegistry {

    private final ApplicationContext context;
    private final ShipEventNotifier shipEventNotifier;
    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;

    public SpringBehaviourFactory(ApplicationContext context, FleetApi fleetApi, ShipEventNotifier shipEventNotifier, SystemsApi systemsApi) {
        this.context = context;
        this.fleetApi = fleetApi;
        this.shipEventNotifier = shipEventNotifier;
        this.systemsApi = systemsApi;
    }

    @Override
    public MiningBehaviourFactory miningAutomation(SystemSymbol system, WaypointType waypointType) {
        return new MiningBehaviourFactory(systemsApi, context.getBean(BehaviourFactoryRegistry.class), system, waypointType);
    }

    @Override
    public ExtractionBehaviourFactory extraction() {
        return new ExtractionBehaviourFactory(fleetApi, shipEventNotifier);
    }

    @Override
    public NavigateBehaviourFactory navigateAutomation(WaypointSymbol waypointSymbol) {
        return new NavigateBehaviourFactory(fleetApi, shipEventNotifier, waypointSymbol);
    }

    @Override
    public OrbitBehaviourFactory orbitAutomation() {
        return new OrbitBehaviourFactory(fleetApi);
    }

    @Override
    public DockBehaviourFactory dock() {
        return new DockBehaviourFactory(fleetApi);
    }

    @Override
    public RefuelBehaviour refuel() {
        return new RefuelBehaviour(fleetApi);
    }
}
