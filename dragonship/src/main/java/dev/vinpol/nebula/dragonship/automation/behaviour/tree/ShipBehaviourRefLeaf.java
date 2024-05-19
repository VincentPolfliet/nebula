package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;

import java.util.Objects;

public final class ShipBehaviourRefLeaf implements ShipLeaf {

    private final String name;
    private final ShipBehaviourRef ref;
    private ShipBehaviourFactoryCreator behaviourFactory;
    private ShipBehaviourLeaf inner;

    public ShipBehaviourRefLeaf(ShipBehaviourRef ref) {
        this.name = null;
        this.ref = ref;
    }

    public ShipBehaviourRefLeaf(String name, ShipBehaviourRef ref) {
        this.name = name;
        this.ref = Objects.requireNonNull(ref);
    }

    public void setBehaviourFactory(ShipBehaviourFactoryCreator behaviourFactory) {
        this.behaviourFactory = behaviourFactory;
    }

    @Override
    public String getName() {
        return name != null ? name : (inner != null ? inner.toString() : "null");
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public ShipBehaviourLeafState act(Ship instance) {
        if (inner == null) {
            inner = new ShipBehaviourLeaf(() -> ref.apply(behaviourFactory).create());
        }

        return inner.act(instance);
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        return act(ship).result();
    }
}
