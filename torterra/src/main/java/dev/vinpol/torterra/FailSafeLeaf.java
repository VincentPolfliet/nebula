package dev.vinpol.torterra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A fail-safe wrapper for a Leaf, ensuring that exceptions thrown during execution are caught and ignored.
 *
 * @param <T> The type of data that this class operates on.
 */
public class FailSafeLeaf<T> implements Leaf<T> {

    private final Logger logger = LoggerFactory.getLogger(FailSafeLeaf.class);

    private final Leaf<T> leaf;

    public FailSafeLeaf(Leaf<T> leaf) {
        super();
        this.leaf = leaf;
    }

    @Override
    public LeafState act(T instance) {
        try {
            LeafState state = leaf.act(instance);

            if (state.isFailure()) {
                return LeafState.success();
            }

            return state;
        } catch (RuntimeException e) {
            logger.warn("Something went wrong with executing '{}'", leaf, e);
            return LeafState.success();
        }
    }

    @Override
    public String toString() {
        return "safe(%s)".formatted(leaf);
    }

    public Leaf<T> getInner() {
        return leaf;
    }
}
