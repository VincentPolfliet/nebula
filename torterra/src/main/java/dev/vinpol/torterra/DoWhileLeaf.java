package dev.vinpol.torterra;

import java.util.function.Predicate;

public class DoWhileLeaf<T> extends Leaf<T> {
    private final Predicate<T> predicate;
    private final Leaf<T> leaf;

    public DoWhileLeaf(Predicate<T> predicate, Leaf<T> leaf) {
        super();
        this.predicate = predicate;
        this.leaf = leaf;
    }

    @Override
    public void act(T instance) {
        while (predicate.test(instance)) {
            leaf.act(instance);

            if (leaf.isFailure()) {
                fail();
                return;
            }
        }

        succeed();
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    public Leaf<T> getLeaf() {
        return leaf;
    }
}
