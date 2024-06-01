package dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;

import java.util.List;

public interface ShipBehaviourTreeFactory {
    ShipBehaviourTree execute(List<ShipBehaviour> behaviours);
}
