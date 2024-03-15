package dev.vinpol.nebula.automation.behaviour.tree;

import dev.vinpol.nebula.automation.behaviour.BehaviourFactoryRegistry;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviourFactory;

import java.util.function.Function;

public interface ShipBehaviourRef extends Function<BehaviourFactoryRegistry, ShipBehaviourFactory> {
}
