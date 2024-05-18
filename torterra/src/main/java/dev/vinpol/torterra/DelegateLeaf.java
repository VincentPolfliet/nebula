package dev.vinpol.torterra;

public class DelegateLeaf<T> implements Leaf<T> {

    private final String name;
    private final Leaf<T> inner;

    public DelegateLeaf(String name, Leaf<T> inner) {
        this.name = name;
        this.inner = inner;
    }

    public DelegateLeaf(Leaf<T> inner) {
        this.name = null;
        this.inner = inner;
    }

    @Override
    public LeafState act(T instance) {
        return inner.act(instance);
    }

    @Override
    public String toString() {
        return String.valueOf(name) + String.valueOf(inner);
    }
}
