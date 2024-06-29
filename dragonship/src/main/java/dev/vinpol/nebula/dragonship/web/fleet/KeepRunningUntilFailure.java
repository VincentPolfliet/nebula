package dev.vinpol.nebula.dragonship.web.fleet;

import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;

import java.util.function.Function;

public class KeepRunningUntilFailure implements ShipBehaviour, Function<Ship, ShipBehaviour> {
    private final ShipAlgorithmResolver shipAlgorithmSolver;

    private ShipBehaviour current;
    private ShipBehaviourResult lastResult;

    public KeepRunningUntilFailure(ShipAlgorithmResolver shipAlgorithmResolver) {
        this.shipAlgorithmSolver = shipAlgorithmResolver;
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        if (current == null || (lastResult != null && lastResult.isDone())) {
            current = shipAlgorithmSolver.resolve(ship.getRole()).decideBehaviour(ship);
        }

        lastResult = current.update(ship);

        if (lastResult.isFailure()) {
            return ShipBehaviourResult.failure("inner has failed");
        }

        return lastResult;
    }

    @Override
    public ShipBehaviour apply(Ship ship) {
        return this;
    }
}
