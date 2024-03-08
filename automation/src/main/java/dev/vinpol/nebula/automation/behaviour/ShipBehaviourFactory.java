package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.spacetraders.sdk.models.Ship;

public interface ShipBehaviourFactory {
    ShipBehaviour create();
}
