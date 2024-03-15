package dev.vinpol.torterra;

public class AbstractLeafIterator<T> implements LeafIterator<T> {

    private final StatefulLeaf<T> statefulLeaf;

    public AbstractLeafIterator(StatefulLeaf<T> statefulLeaf) {
        this.statefulLeaf = statefulLeaf;
    }

    @Override
    public boolean hasNext() {
        return statefulLeaf.isRunning();
    }

    @Override
    public Leaf<T> current() {
        return statefulLeaf;
    }

    @Override
    public LeafState act(T instance) {
        return statefulLeaf.act(instance);
    }

    @Override
    public String toString() {
        return statefulLeaf.toString();
    }
}
