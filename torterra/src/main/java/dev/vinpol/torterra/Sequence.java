package dev.vinpol.torterra;

import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Sequence<T> extends StatefulLeaf<T> implements IterableLeaf<T>, LeafIterator<T> {
    private final Iterable<Leaf<T>> leaves;
    private final Iterator<Leaf<T>> iterator;
    @Getter
    private Leaf<T> current;

    public Sequence(Iterable<Leaf<T>> leaves) {
        super();
        this.leaves = Objects.requireNonNull(leaves);
        this.iterator = leaves.iterator();
    }

    @Override
    public void doAct(T instance) {
        current = null;

        if (isRunning() && iterator.hasNext()) {
            current = iterator.next();
            LeafState currentStepState = current.act(instance);
            // because LeafState is an interface and we want to propagate the result, use the current step's result as the current sequence

            setState(currentStepState);
        }

        if (isRunning() && !iterator.hasNext()) {
            succeed();
        }
    }

    @Override
    protected void fail() {
        super.fail();
    }

    @Override
    public String toString() {
        return "sequence(%s)".formatted(leaves);
    }

    @Override
    public LeafIterator<T> leafIterator() {
        return this;
    }

    @Nonnull
    @Override
    public Iterator<Leaf<T>> iterator() {
        return leaves.iterator();
    }

    @Override
    public boolean hasNext() {
        return isRunning();
    }

    @Override
    public Leaf<T> current() {
        return current;
    }

    @Override
    public LeafState next(T instance) {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return act(instance);
    }
}
