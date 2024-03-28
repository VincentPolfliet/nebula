package dev.vinpol.nebula.automation.behaviour.tree;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.Leaf;

public final class ShipBehaviourLeafs {
    private ShipBehaviourLeafs() {

    }

    public static Leaf<Ship> orbit() {
        return new ShipBehaviourRefLeaf(ShipBehaviourFactoryCreator::orbitAutomation);
    }

    public static Leaf<Ship> extraction() {
        return new ShipBehaviourRefLeaf(ShipBehaviourFactoryCreator::extraction);
    }

    public static Leaf<Ship> dock() {
        return new ShipBehaviourRefLeaf(ShipBehaviourFactoryCreator::dock);
    }

    public static Leaf<Ship> refuel() {
        return new ShipBehaviourRefLeaf(ShipBehaviourFactoryCreator::refuel);
    }

    public static Leaf<Ship> navigate(WaypointSymbol waypointSymbol) {
        return new ShipBehaviourRefLeaf((registry) -> registry.navigateAutomation(waypointSymbol));
    }
}
