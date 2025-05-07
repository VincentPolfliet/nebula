package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor.ShipBehaviourTree;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor.ShipBehaviourTreeFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor.fbt.ShipFBTFactory;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.List;
import java.util.Objects;

@NotThreadSafe
public class ShipTreeBehaviour implements ShipBehaviour {

    private final Logger logger = LoggerFactory.getLogger(ShipTreeBehaviour.class);

    private final ShipBehaviourTreeFactory executor;
    private final ShipBehaviourTree tree;

    public ShipTreeBehaviour(ShipBehaviour... behaviours) {
        this(List.of(behaviours));
    }

    public ShipTreeBehaviour(List<ShipBehaviour> behaviours) {
        this.executor = new ShipFBTFactory();
        this.tree = executor.execute(behaviours);
    }

    @Override
    public ShipBehaviorResult update(Ship ship) {
        Objects.requireNonNull(ship);

        return tree.update(ship);
    }
}
