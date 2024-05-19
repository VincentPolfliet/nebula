package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.Leaf;

public interface ShipLeaf extends Leaf<Ship>, ShipBehaviour {
    @Override
    default ShipBehaviourLeafState act(Ship instance) {
        return new ShipBehaviourLeafState(update(instance));
    }

    @Override
    ShipBehaviourResult update(Ship ship);
}
