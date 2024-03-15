package dev.vinpol.torterra;

import java.util.Objects;

public class SafeLeafIterator<T> implements LeafIterator<T> {

    private final LeafIterator<T> iterator;

    SafeLeafIterator(LeafIterator<T> iterator) {
        this.iterator = Objects.requireNonNull(iterator);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Leaf<T> current() {
        return iterator.current();
    }

    @Override
    public LeafState act(T instance) {
        LeafState state = iterator.act(instance);

        if (state == LeafState.FAILED) {
            return LeafState.SUCCESS;
        }

        return state;
    }

    @Override
    public String toString() {
        return "safe(%s)".formatted(iterator);
    }
}
