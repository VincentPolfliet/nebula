package dev.vinpol.nebula.automation.tree;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.Leaf;
import dev.vinpol.torterra.Torterra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ShipTreeBehaviour implements ShipBehaviour {

    private final Logger logger = LoggerFactory.getLogger(ShipTreeBehaviour.class);

    private final Torterra.Tree<Ship> tree;

    public ShipTreeBehaviour(Torterra.Tree<Ship> tree) {
        this.tree = Objects.requireNonNull(tree);
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        Objects.requireNonNull(ship);

        Leaf<Ship> currentStep = tree.current();
        if (currentStep != null) {
            logger.debug("currentStep: {}", currentStep);

            tree.tick(ship);
            logger.debug("state: {}", currentStep.getState());

            if (currentStep.isFailure()) {
                return ShipBehaviourResult.failure();
            } else if (currentStep.isSuccess()) {
                // TODO: no bueno
                if (currentStep instanceof AutomationLeaf automation) {
                    ShipBehaviourResult directResult = automation.getResult();

                    if (directResult.isDone()) {
                        return ShipBehaviourResult.success();
                    }

                    return directResult;
                }

                return ShipBehaviourResult.success();
            }
        }

        return ShipBehaviourResult.done();
    }
}
