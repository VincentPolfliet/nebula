package dev.vinpol.nebula.automation.behaviour.tree;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class ShipSequenceBehaviour implements ShipBehaviour {

    private final Logger logger = LoggerFactory.getLogger(ShipSequenceBehaviour.class);

    private final List<Leaf<Ship>> leaves;
    private final LeafIterator<Ship> iterator;

    public ShipSequenceBehaviour(List<Leaf<Ship>> leaves) {
        this.leaves = Objects.requireNonNull(leaves);
        this.iterator = new ComboLeafIterator<>(toIterators(leaves));
    }

    private List<LeafIterator<Ship>> toIterators(List<Leaf<Ship>> leaves) {
        return leaves
            .stream()
            .map(LeafIterator::tryWrap)
            .toList();
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        Objects.requireNonNull(ship);

        if (!iterator.hasNext()) {
            return ShipBehaviourResult.done();
        }

        LeafState state = iterator.act(ship);

        Leaf<Ship> current = iterator.current();
        logger.debug("current: {}", current);
        logger.debug("current instance: {}", current.getClass());

        switch (state) {
            case SUCCESS, RUNNING -> {
                return getInnerResult(current);
            }
            case FAILED -> {
                return ShipBehaviourResult.failure();
            }

            default -> throw new IllegalStateException("Unexpected value: " + ship);
        }
    }

    private static ShipBehaviourResult getInnerResult(Leaf<Ship> leaf) {
        Leaf<Ship> unwrappedLeaf = TorterraUtils.unwrap(leaf);

        // TODO: combine this in some way
        if (unwrappedLeaf instanceof ShipBehaviourLeaf shipLeaf) {
            var innerResult = shipLeaf.getResult();
            return innerResult.isDone() ? ShipBehaviourResult.success() : innerResult;
        }

        if (unwrappedLeaf instanceof ShipBehaviourRefLeaf refLeaf) {
            var innerResult = refLeaf.getResult();
            return innerResult.isDone() ? ShipBehaviourResult.success() : innerResult;
        }

        return ShipBehaviourResult.success();
    }
}
