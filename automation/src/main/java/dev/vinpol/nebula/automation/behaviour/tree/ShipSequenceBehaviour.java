package dev.vinpol.nebula.automation.behaviour.tree;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ShipSequenceBehaviour implements ShipBehaviour {

    private final Logger logger = LoggerFactory.getLogger(ShipSequenceBehaviour.class);

    private final LeafIterator<Ship> iterator;

    public ShipSequenceBehaviour(List<Leaf<Ship>> leaves) {
        Objects.requireNonNull(leaves);
        this.iterator = new ComboLeafIterator<>(toIterators(leaves));
    }

    private List<LeafIterator<Ship>> toIterators(List<Leaf<Ship>> leaves) {
        return leaves
            .stream()
            .map(ShipSequenceBehaviour::tryWrap)
            .toList();
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        Objects.requireNonNull(ship);

        if (!iterator.hasNext()) {
            logger.trace("behaviour has no more steps left, returning done");
            return ShipBehaviourResult.done();
        }

        LeafState state = iterator.act(ship);
        logger.debug("state: {}", state);

        Leaf<Ship> current = iterator.current();
        logger.debug("current: {}", current);
        logger.debug("current instance: {}", current.getClass());

        ShipBehaviourResult innerResult = switch (state) {
            // Running is handled as a "getInnerResult" because else things like 'Sequence' or 'Selector' wouldn't work
            case SUCCESS, RUNNING -> getInnerResult(current);
            case FAILED -> ShipBehaviourResult.failure();
        };

        logger.debug("innerResult: {}", innerResult);
        return innerResult;
    }

    private static ShipBehaviourResult getInnerResult(Leaf<Ship> leaf) {
        Leaf<Ship> unwrappedLeaf = TorterraUtils.unwrap(leaf);

        if (unwrappedLeaf instanceof ShipBehaviourLeaf shipLeaf) {
            return extractResult(shipLeaf::getResult);
        } else if (unwrappedLeaf instanceof ShipBehaviourRefLeaf refLeaf) {
            return extractResult(refLeaf::getResult);
        }

        return ShipBehaviourResult.success();
    }

    private static ShipBehaviourResult extractResult(Supplier<ShipBehaviourResult> supplier) {
        ShipBehaviourResult innerResult = supplier.get();
        // 'Done' should only be sent by the final behaviour,
        // we interpreted it as a success so the parent behaviour can continue
        return innerResult.isDone() ? ShipBehaviourResult.success() : innerResult;
    }

    private static <T> LeafIterator<T> tryWrap(Leaf<T> leaf) {
        boolean isWrappedInFailSafe = leaf instanceof FailSafeLeaf<T>;
        Leaf<T> innerLeaf = TorterraUtils.unwrap(leaf);

        LeafIterator<T> iteratorToUse = innerLeaf instanceof IterableLeaf<T> iterable ? iterable.leafIterator() : LeafIterator.singleton(innerLeaf);
        return isWrappedInFailSafe ? LeafIterator.safe(iteratorToUse) : iteratorToUse;
    }
}
