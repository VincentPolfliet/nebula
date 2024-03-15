package dev.vinpol.torterra;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A composite iterator that combines multiple LeafIterators.
 *
 * @param <T> The type of elements in the iterators.
 */
public class ComboLeafIterator<T> implements LeafIterator<T> {

    private final Iterator<LeafIterator<T>> leafIterators;
    private LeafIterator<T> currentIterator;

    public ComboLeafIterator(List<LeafIterator<T>> leafIterators) {
        this.leafIterators = Objects.requireNonNull(leafIterators).iterator();
        this.currentIterator = null;
    }

    /**
     * Checks if there are more elements in the combined iterators.
     *
     * @return true if there are more elements, false otherwise.
     */
    @Override
    public boolean hasNext() {
        if (currentIterator == null || !currentIterator.hasNext()) {
            while (leafIterators.hasNext()) {
                currentIterator = leafIterators.next();

                if (currentIterator.hasNext()) {
                    return true;
                }
            }

            return false;
        }

        return true;
    }

    @Override
    public Leaf<T> current() {
        return currentIterator.current();
    }

    /**
     * Performs an action on the next element in the combined iterators.
     *
     * @param instance The instance on which to perform the action.
     * @return The state of the leaf after the action.
     * @throws NoSuchElementException if no elements are left
     */
    @Override
    public LeafState act(T instance) {
        // If the current iterator is null or has no more elements, move to the next one
        while (currentIterator == null || !currentIterator.hasNext()) {
            currentIterator = leafIterators.next();
        }

        return currentIterator.act(instance);
    }

    @Override
    public String toString() {
        return String.valueOf(currentIterator);
    }
}
