package dev.vinpol.nebula.automation.tree;

import dev.vinpol.nebula.automation.behaviour.BehaviourFactoryRegistry;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.torterra.Torterra;

public class AutomationTorterra implements Torterra {

    private final BehaviourFactoryRegistry automationFactory;

    public AutomationTorterra(BehaviourFactoryRegistry automationFactory) {
        this.automationFactory = automationFactory;
    }


    public AutomationLeaf extraction() {
        return fromFactory(automationFactory.extraction());
    }

    public AutomationLeaf navigate(WaypointSymbol waypointSymbol) {
        return fromFactory(automationFactory.navigateAutomation(waypointSymbol));
    }

    public AutomationLeaf orbit() {
        return fromFactory(automationFactory.orbitAutomation());
    }

    public AutomationLeaf dock() {
        return fromFactory(automationFactory.dock());
    }

    public AutomationLeaf refuel() {
        return fromFactory(automationFactory.refuel());
    }

    private static AutomationLeaf fromFactory(ShipBehaviourFactory factory) {
        return new AutomationLeaf(factory.create());
    }
}
