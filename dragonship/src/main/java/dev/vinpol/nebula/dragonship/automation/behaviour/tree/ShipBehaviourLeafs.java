package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.AutomationFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.models.TradeSymbol;

public final class ShipBehaviourLeafs {
    private ShipBehaviourLeafs() {

    }

    public static ShipBehaviour orbit() {
        return new ShipBehaviourRefLeaf("orbit", AutomationFactory::orbitAutomation);
    }

    public static ShipBehaviour extraction() {
        return new ShipBehaviourRefLeaf("extraction", AutomationFactory::extraction);
    }

    public static ShipBehaviour dock() {
        return new ShipBehaviourRefLeaf("dock", AutomationFactory::dock);
    }

    public static ShipBehaviour refuel() {
        return new ShipBehaviourRefLeaf("refuel", AutomationFactory::refuel);
    }

    public static ShipBehaviour navigate(WaypointSymbol waypoint) {
        return new ShipBehaviourRefLeaf("navigate", registry -> registry.navigateAutomation(waypoint));
    }

    public static ShipBehaviour sellCargo(TradeSymbol tradeSymbol, int units) {
        return new ShipBehaviourRefLeaf("sellCargo %s (%dx)".formatted(tradeSymbol, units), (registry) -> registry.sellCargo(tradeSymbol, units));
    }
}
