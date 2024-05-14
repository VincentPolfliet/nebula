package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactoryCreator;

import java.util.function.Function;

public interface ShipBehaviourRef extends Function<ShipBehaviourFactoryCreator, ShipBehaviourFactory> {
}
