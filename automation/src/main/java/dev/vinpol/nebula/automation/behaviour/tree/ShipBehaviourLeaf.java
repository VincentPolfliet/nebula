package dev.vinpol.nebula.automation.behaviour.tree;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.Leaf;
import dev.vinpol.torterra.LeafState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class ShipBehaviourLeaf implements Leaf<Ship> {

    private final Supplier<ShipBehaviour> behaviourSupplier;
    private ShipBehaviour behaviour;
    private ShipBehaviourResult behaviourResult;
    private LeafState leafState;

    public ShipBehaviourLeaf(Supplier<ShipBehaviour> behaviourSupplier) {
        super();
        this.behaviourSupplier = behaviourSupplier;
    }

    @Override
    public LeafState act(Ship ship) {
        if (leafState != null) {
            return leafState;
        }

        this.behaviour = behaviourSupplier.get();
        this.behaviourResult = behaviour.update(ship);
        this.leafState = handleLeafState(behaviourResult);

        return leafState;
    }

    private LeafState handleLeafState(ShipBehaviourResult result) {
        if (result.isFailure()) {
            return LeafState.FAILED;
        }

        return LeafState.SUCCESS;
    }

    public ShipBehaviourResult getResult() {
        return behaviourResult;
    }

    @Override
    public String toString() {
        return behaviour.getName();
    }
}
