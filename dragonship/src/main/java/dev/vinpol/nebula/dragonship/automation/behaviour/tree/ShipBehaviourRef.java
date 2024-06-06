package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.AutomationFactory;

import java.util.function.Function;

public interface ShipBehaviourRef extends Function<AutomationFactory, ShipBehaviourFactory> {
}
