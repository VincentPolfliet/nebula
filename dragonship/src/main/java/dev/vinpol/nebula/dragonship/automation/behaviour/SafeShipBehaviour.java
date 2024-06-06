package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;

public class SafeShipBehaviour implements ShipBehaviour {
    private final ShipBehaviour behaviour;

    public SafeShipBehaviour(ShipBehaviour behaviour) {
        this.behaviour = behaviour;
    }

    @Override
    public String getName() {
        return "try" + behaviour.getName();
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        ShipBehaviourResult result = behaviour.update(ship);

        if (result.isFailure()) {
            return ShipBehaviourResult.success();
        }

        return result;
    }

    public ShipBehaviour unwrap() {
        return behaviour;
    }
}
