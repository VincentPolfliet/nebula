package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.AutomationFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.spacetraders.sdk.models.Ship;

import java.util.Objects;

public final class ShipBehaviourRefLeaf implements ShipBehaviour {

    private final String name;
    private final ShipBehaviourRef ref;
    private AutomationFactory behaviourFactory;
    private ShipBehaviour inner;

    public ShipBehaviourRefLeaf(ShipBehaviourRef ref) {
        this.name = null;
        this.ref = ref;
    }

    public ShipBehaviourRefLeaf(String name, ShipBehaviourRef ref) {
        this.name = name;
        this.ref = Objects.requireNonNull(ref);
    }

    public void setBehaviourFactory(AutomationFactory behaviourFactory) {
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
    public ShipBehaviorResult update(Ship ship) {
        if (inner == null) {
            ShipBehaviourFactory factory = ref.apply(behaviourFactory);
            inner = factory.create();
        }

        return inner.update(ship);
    }
}
