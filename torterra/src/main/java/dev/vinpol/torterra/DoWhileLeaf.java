package dev.vinpol.torterra;

import java.util.function.Predicate;

public class DoWhileLeaf<T> extends StatefulLeaf<T> {
    private final Predicate<T> predicate;
    private final Leaf<T> leaf;

    public DoWhileLeaf(Predicate<T> predicate, Leaf<T> leaf) {
        super();
        this.predicate = predicate;
        this.leaf = leaf;
    }

    @Override
    public void doAct(T instance) {
        while (predicate.test(instance)) {
            LeafState state = leaf.act(instance);

            if (state == LeafState.FAILED) {
                fail();
                return;
            }
        }

        succeed();
    }
}
