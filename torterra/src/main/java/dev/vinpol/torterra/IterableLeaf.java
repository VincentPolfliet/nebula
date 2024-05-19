package dev.vinpol.torterra;

import javax.annotation.Nonnull;
import java.util.Iterator;

public interface IterableLeaf<T> extends Leaf<T>, Iterable<Leaf<T>> {
    LeafIterator<T> leafIterator();

    @Nonnull
    @Override
    Iterator<Leaf<T>> iterator();
}
