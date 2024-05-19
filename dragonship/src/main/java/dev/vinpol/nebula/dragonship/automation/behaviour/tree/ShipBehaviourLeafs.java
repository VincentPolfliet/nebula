package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;

public final class ShipBehaviourLeafs {
    private ShipBehaviourLeafs() {

    }

    public static ShipBehaviourRefLeaf orbit() {
        return new ShipBehaviourRefLeaf("orbit", ShipBehaviourFactoryCreator::orbitAutomation);
    }

    public static ShipBehaviourRefLeaf extraction() {
        return new ShipBehaviourRefLeaf("extraction", ShipBehaviourFactoryCreator::extraction);
    }

    public static ShipBehaviourRefLeaf dock() {
        return new ShipBehaviourRefLeaf("dock", ShipBehaviourFactoryCreator::dock);
    }

    public static ShipBehaviourRefLeaf refuel() {
        return new ShipBehaviourRefLeaf("refuel", ShipBehaviourFactoryCreator::refuel);
    }

    public static ShipBehaviourRefLeaf navigate(final WaypointSymbol waypointSymbol) {
        return new ShipBehaviourRefLeaf("navigate", (registry) -> registry.navigateAutomation(waypointSymbol));
    }
}
