package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;

import java.util.function.Supplier;

public final class ShipBehaviourLeaf implements ShipLeaf {

    private final Supplier<ShipBehaviour> behaviourSupplier;
    private ShipBehaviour behaviour;

    public ShipBehaviourLeaf(Supplier<ShipBehaviour> behaviourSupplier) {
        super();
        this.behaviourSupplier = behaviourSupplier;
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        return behaviourSupplier.get().update(ship);
    }

    @Override
    public String toString() {
        return behaviour != null ? behaviour.getName() : String.valueOf(behaviourSupplier);
    }
}
