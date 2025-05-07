package dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.spacetraders.sdk.models.Ship;

public interface ShipBehaviourTree extends ShipBehaviour {
    ShipBehaviorResult update(Ship ship);
}
