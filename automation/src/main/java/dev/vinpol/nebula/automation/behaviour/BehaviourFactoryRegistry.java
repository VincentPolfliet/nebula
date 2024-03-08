package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.models.WaypointType;

public interface BehaviourFactoryRegistry {

    MiningBehaviourFactory miningAutomation(SystemSymbol system, WaypointType waypointType);

    ExtractionBehaviourFactory extraction();

    NavigateBehaviourFactory navigateAutomation(WaypointSymbol waypointSymbol);

    OrbitBehaviourFactory orbitAutomation();

    DockBehaviourFactory dock();

    RefuelBehaviour refuel();
}
