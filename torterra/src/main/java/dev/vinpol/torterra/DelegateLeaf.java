package dev.vinpol.torterra;

public class DelegateLeaf<T> extends Leaf<T> {

    private final Leaf<T> inner;

    public DelegateLeaf(String name, Leaf<T> inner) {
        super(name);
        this.inner = inner;
    }

    public DelegateLeaf(Leaf<T> inner) {
        this.inner = inner;
    }

    @Override
    public void act(T instance) {
        inner.act(instance);

        TorterraUtils.transferState(inner, this);
    }
}
