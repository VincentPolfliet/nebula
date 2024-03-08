package dev.vinpol.torterra;

class Sequence<T> extends Leaf<T> {
    private final Iterable<Leaf<T>> leaves;

    public Sequence(Iterable<Leaf<T>> leaves) {
        super();
        this.leaves = leaves;
    }

    @Override
    public void act(T instance) {
        for (Leaf<T> leaf : leaves) {
            leaf.act(instance);

            if (leaf.isFailure()) {
                fail();
                return;
            }
        }

        succeed();
    }
}
