package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
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
    public ShipBehaviorResult update(Ship ship) {
        ShipBehaviorResult result = behaviour.update(ship);

        if (result.isFailure()) {
            return ShipBehaviorResult.success();
        }

        return result;
    }

    public ShipBehaviour unwrap() {
        return behaviour;
    }
}
