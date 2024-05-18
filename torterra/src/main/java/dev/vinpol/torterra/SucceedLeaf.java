package dev.vinpol.torterra;

class SucceedLeaf<T> implements Leaf<T> {

    @Override
    public LeafState act(T instance) {
        return LeafState.success();
    }

    @Override
    public String toString() {
        return "succeed";
    }
}
