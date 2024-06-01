package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.models.TradeSymbol;

import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourSequence.sequence;

public final class ShipBehaviourLeafs {
    private ShipBehaviourLeafs() {

    }

    public static ShipBehaviour orbit() {
        return sequence("tryOrbit",
            ShipLeafs.isNotInOrbit(),
            new ShipBehaviourRefLeaf("orbit", ShipBehaviourFactoryCreator::orbitAutomation)
        );
    }

    public static ShipBehaviour extraction() {
        return new ShipBehaviourRefLeaf("extraction", ShipBehaviourFactoryCreator::extraction);
    }

    public static ShipBehaviour dock() {
        return sequence("tryDock",
            ShipLeafs.isNotDocked(),
            new ShipBehaviourRefLeaf("dock", ShipBehaviourFactoryCreator::dock)
        );
    }

    public static ShipBehaviour refuel() {
        return sequence("tryRefuel",
            ShipLeafs.fuelIsNotFull(),
            new ShipBehaviourRefLeaf("refuel", ShipBehaviourFactoryCreator::refuel)
        );
    }

    public static ShipBehaviour navigate(WaypointSymbol waypoint) {
        return sequence("tryNavigate",
            ShipLeafs.isNotAtLocation(waypoint),
            new ShipBehaviourRefLeaf("navigate", registry -> registry.navigateAutomation(waypoint))
        );
    }

    public static ShipBehaviour sellCargo(TradeSymbol tradeSymbol, int units) {
        return new ShipBehaviourRefLeaf("sellCargo %s (%dx)".formatted(tradeSymbol, units), (registry) -> registry.sellCargo(tradeSymbol, units));
    }
}
