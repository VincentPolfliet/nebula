package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.Leaf;

import java.util.List;

import static dev.vinpol.torterra.Torterra.safeSequence;
import static dev.vinpol.torterra.Torterra.sequence;

public final class ShipBehaviourLeafs {
    private ShipBehaviourLeafs() {

    }

    public static ShipBehaviourRefLeaf orbit() {
        return new ShipBehaviourRefLeaf(ShipBehaviourFactoryCreator::orbitAutomation);
    }

    public static ShipBehaviourRefLeaf extraction() {
        return new ShipBehaviourRefLeaf(ShipBehaviourFactoryCreator::extraction);
    }

    public static ShipBehaviourRefLeaf dock() {
        return new ShipBehaviourRefLeaf(ShipBehaviourFactoryCreator::dock);
    }

    public static ShipBehaviourRefLeaf refuel() {
        return new ShipBehaviourRefLeaf(ShipBehaviourFactoryCreator::refuel);
    }

    public static ShipBehaviourRefLeaf navigate(final WaypointSymbol waypointSymbol) {
        return new ShipBehaviourRefLeaf((registry) -> registry.navigateAutomation(waypointSymbol));
    }
}
