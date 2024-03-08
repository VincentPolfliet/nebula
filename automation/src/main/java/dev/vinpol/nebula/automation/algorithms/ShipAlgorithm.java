package dev.vinpol.nebula.automation.algorithms;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipRole;

public interface ShipAlgorithm {
    ShipRole forRole();

    ShipBehaviour decideBehaviour(Ship ship);
}
