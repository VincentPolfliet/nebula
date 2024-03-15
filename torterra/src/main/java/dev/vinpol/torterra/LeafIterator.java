package dev.vinpol.torterra;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public interface LeafIterator<T> {
    boolean hasNext();

    Leaf<T> current();

    LeafState act(T instance);

    static <T> LeafIterator<T> singleton(Leaf<T> leaf) {
        return new SingletonLeafIterator<>(leaf);
    }

    @SafeVarargs
    static <T> LeafIterator<T> of(Leaf<T>... leaves) {
        return ofIterator(List.of(leaves).iterator());
    }

    static <T> LeafIterator<T> ofIterator(Iterator<Leaf<T>> iterator) {
        return new IteratorAdapter<>(iterator);
    }

    static <T> LeafIterator<T> safe(LeafIterator<T> original) {
        return new SafeLeafIterator<>(original);
    }

    static <T> LeafIterator<T> tryWrap(Leaf<T> leaf) {
        boolean isWrappedInFailSafe = leaf.isFailSafe();
        Leaf<T> innerLeaf = TorterraUtils.unwrap(leaf);

        LeafIterator<T> iteratorToUse = innerLeaf instanceof IterableLeaf<T> iterable ? iterable.leafIterator() : LeafIterator.singleton(innerLeaf);
        return isWrappedInFailSafe ? LeafIterator.safe(iteratorToUse) : iteratorToUse;
    }

    class SingletonLeafIterator<T> implements LeafIterator<T> {

        private final Leaf<T> inner;
        private boolean called = false;

        public SingletonLeafIterator(Leaf<T> inner) {
            this.inner = inner;
        }

        @Override
        public boolean hasNext() {
            return !called;
        }

        @Override
        public Leaf<T> current() {
            return called ? inner : null;
        }

        @Override
        public LeafState act(T instance) {
            called = true;
            return inner.act(instance);
        }
    }

    class IteratorAdapter<T> implements LeafIterator<T> {

        private final Iterator<Leaf<T>> iterator;
        private Leaf<T> current;

        public IteratorAdapter(Iterator<Leaf<T>> iterator) {
            this.iterator = Objects.requireNonNull(iterator, "iterator");
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Leaf<T> current() {
            return current;
        }

        @Override
        public LeafState act(T instance) {
            current = iterator.next();
            return current.act(instance);
        }
    }
}
