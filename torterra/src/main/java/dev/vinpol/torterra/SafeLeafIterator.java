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
        LeafState state = iterator.next(instance);

        if (state instanceof FailedState) {
            return LeafState.success();
        }

        return state;
    }

    @Override
    public String toString() {
        return "safe(%s)".formatted(iterator);
    }
}
