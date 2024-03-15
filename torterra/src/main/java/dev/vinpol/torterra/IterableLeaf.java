package dev.vinpol.torterra;

import java.util.Iterator;

public interface IterableLeaf<T> extends Leaf<T>, Iterable<Leaf<T>> {
    LeafIterator<T> leafIterator();

    @Override
    Iterator<Leaf<T>> iterator();
}
