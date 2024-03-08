package dev.vinpol.torterra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A fail-safe wrapper for a Leaf, ensuring that exceptions thrown during execution are caught and ignored.
 *
 * @param <T> The type of data that this class operates on.
 */
class FailSafeLeaf<T> extends Leaf<T> {

    private final Logger logger = LoggerFactory.getLogger(FailSafeLeaf.class);

    private final Leaf<T> leaf;

    public FailSafeLeaf(Leaf<T> leaf) {
        super();
        this.leaf = leaf;
    }

    @Override
    public void act(T instance) {
        try {
            leaf.act(instance);
        } catch (RuntimeException e) {
            logger.warn(e.getMessage());
        }

        succeed();
    }
}
