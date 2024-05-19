package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ShipSequenceBehaviour implements ShipBehaviour {

    private final Logger logger = LoggerFactory.getLogger(ShipSequenceBehaviour.class);

    private final ComboLeafIterator<Ship> currentIterator;

    @SafeVarargs
    public ShipSequenceBehaviour(Leaf<Ship>... leaves) {
        this(List.of(leaves));
    }

    public ShipSequenceBehaviour(List<Leaf<Ship>> leaves) {
        Objects.requireNonNull(leaves);
        this.currentIterator = new ComboLeafIterator<>(toIterators(leaves));
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

        if (!currentIterator.hasNext()) {
            logger.debug("behaviour has no more steps left, returning done");
            return ShipBehaviourResult.done();
        }

        logger.debug("iterator: {}", currentIterator);

        LeafState state = currentIterator.act(ship);
        logger.debug("state: {}", state);

        Leaf<Ship> currentStep = currentIterator.current();
        logger.debug("current: {}", currentStep);
        logger.debug("current instance: {}", currentStep.getClass());

        ShipBehaviourResult innerResult = extractInnerResult(currentStep, state);

        logger.debug("innerResult: {}", innerResult);
        return innerResult;
    }

    private ShipBehaviourResult extractInnerResult(Leaf<Ship> currentStep, LeafState state) {
        if (state instanceof ShipBehaviourLeafState behaviourResult) {
            return extractResult(behaviourResult::result);
        }

        if (state.isRunning() || state.isSuccess()) {
            return ShipBehaviourResult.success();
        }

        if (state.isFailure()) {
            // TODO: check if there is a better way to do this
            // if the sequence fails, we still need to continue, it will returns hasNext = false so the ComboLeafIterator will rotate to the next Iterator
            if (currentIterator.currentIterator() instanceof Sequence<Ship>) {
                return ShipBehaviourResult.success();
            }

            return ShipBehaviourResult.failure("Leaf '%s' has result FAILED".formatted(currentStep.toString()));
        }

        throw new IllegalStateException("'%s' is not supported".formatted(state));
    }

    private ShipBehaviourResult extractResult(Supplier<ShipBehaviourResult> supplier) {
        ShipBehaviourResult innerResult = supplier.get();
        logger.debug("innerResult before isDoneConversion: {}", innerResult);
        // 'Done' should only be sent by the final behaviour,
        // we interpreted it as a success so the parent behaviour can continue
        return innerResult.isDone() ? ShipBehaviourResult.success() : innerResult;
    }

    private static <T> LeafIterator<T> tryWrap(Leaf<T> leaf) {
        boolean isWrappedInFailSafe = leaf instanceof FailSafeLeaf<T>;
        Leaf<T> innerLeaf = Tortilla.unwrap(leaf);

        LeafIterator<T> iteratorToUse = innerLeaf instanceof IterableLeaf<T> iterable ? iterable.leafIterator() : LeafIterator.singleton(innerLeaf);
        return isWrappedInFailSafe ? LeafIterator.safe(iteratorToUse) : iteratorToUse;
    }

    @Override
    public String getName() {
        return "ShipSequenceBehaviour{" + currentIterator != null ? String.valueOf(currentIterator.current()) : "null" + "}";
    }
}
