package dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;

public interface ShipBehaviourTree extends ShipBehaviour {
    ShipBehaviourResult update(Ship ship);
}
