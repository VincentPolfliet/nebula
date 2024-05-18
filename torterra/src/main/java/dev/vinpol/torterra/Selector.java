package dev.vinpol.torterra;

import lombok.Getter;

import java.util.Iterator;
import java.util.Objects;

public class Selector<T> extends StatefulLeaf<T> implements IterableLeaf<T>, LeafIterator<T> {


    private final Iterable<Leaf<T>> leaves;
    private final Iterator<Leaf<T>> iterator;
    @Getter
    private Leaf<T> current;

    public Selector(Iterable<Leaf<T>> leaves) {
        this.leaves = Objects.requireNonNull(leaves);
        this.iterator = leaves.iterator();
    }

    @Override
    public void doAct(T instance) {
        current = null;

        if (!isRunning()) {
            return;
        }

        if (isRunning() && iterator.hasNext()) {
            current = iterator.next();
            LeafState result = current.act(instance);

            if (result instanceof SuccessState) {
                succeed();
            }
        }

        if (isRunning() && !iterator.hasNext()) {
            fail();
        }
    }

    @Override
    public LeafIterator<T> leafIterator() {
        return this;
    }

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
}
