package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;

import java.util.List;
import java.util.Objects;

public final class ShipBehaviourRefLeaf implements ShipBehaviour {

    private final String name;
    private final ShipBehaviourRef ref;
    private ShipBehaviourFactoryCreator behaviourFactory;
    private ShipBehaviour inner;

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
    public ShipBehaviourResult update(Ship ship) {
        if (inner == null) {
            ShipBehaviourFactory factory = ref.apply(behaviourFactory);
            inner = factory.create();
        }

        return inner.update(ship);
    }
}
