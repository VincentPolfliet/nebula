package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.Leaf;
import dev.vinpol.torterra.LeafState;

import java.util.Objects;

public final class ShipBehaviourRefLeaf implements Leaf<Ship>, ShipBehaviour {

    private final ShipBehaviourRef ref;
    private ShipBehaviourFactoryCreator behaviourFactory;
    private ShipBehaviourLeaf inner;

    public ShipBehaviourRefLeaf(ShipBehaviourRef ref) {
        this.ref = Objects.requireNonNull(ref);
    }

    public void setBehaviourFactory(ShipBehaviourFactoryCreator behaviourFactory) {
        this.behaviourFactory = behaviourFactory;
    }

    @Override
    public LeafState act(Ship ship) {
        if (inner == null) {
            inner = new ShipBehaviourLeaf(() -> ref.apply(behaviourFactory).create());
        }

        return inner.act(ship);
    }

    public ShipBehaviourResult getResult() {
        return inner.getResult();
    }

    @Override
    public String getName() {
        return inner != null ? inner.toString() : "null";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        act(ship);
        return getResult();
    }
}
