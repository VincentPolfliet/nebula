package dev.vinpol.nebula.dragonship.automation.algorithms;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipRole;

public interface ShipAlgorithm {
    ShipRole forRole();

    ShipBehaviour decideBehaviour(Ship ship);
}
