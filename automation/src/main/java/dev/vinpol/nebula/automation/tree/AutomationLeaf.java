package dev.vinpol.nebula.automation.tree;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.Leaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AutomationLeaf extends Leaf<Ship> {

    private final Logger logger = LoggerFactory.getLogger(AutomationLeaf.class);

    private final ShipBehaviour behaviour;
    private ShipBehaviourResult result;

    public AutomationLeaf(ShipBehaviour behaviour) {
        super();
        this.behaviour = behaviour;
    }

    @Override
    public void act(Ship ship) {
        try {
            ShipBehaviourResult result = behaviour.update(ship);
            this.result = result;

            switch (result) {
                case ShipBehaviourResult.Failed failed:
                    fail();
                    break;
                case ShipBehaviourResult.Success success:
                    succeed();
                    break;
                case ShipBehaviourResult.Done done:
                    succeed();
                    break;
                case ShipBehaviourResult.WaitUntil wait:
                    succeed();
                    break;
            }
        } catch (RuntimeException e) {
            logger.error("Something went wrong with running the automation", e);
            fail();
        }
    }

    public ShipBehaviourResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        return behaviour.getClass().getSimpleName();
    }
}
