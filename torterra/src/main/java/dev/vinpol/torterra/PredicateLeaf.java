package dev.vinpol.torterra;

import java.util.function.Predicate;

class PredicateLeaf<T> implements Leaf<T> {

    private final Predicate<T> predicate;
    private final Leaf<T> onTrue;
    private final Leaf<T> onFalse;

    public PredicateLeaf(Predicate<T> predicate, Leaf<T> onTrue, Leaf<T> onFalse) {
        super();
        this.predicate = predicate;
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    @Override
    public LeafState act(T instance) {
        return predicate.test(instance) ? onTrue.act(instance) : onFalse.act(instance);
    }

    @Override
    public String toString() {
        return "test(onTrue: '%s', onFalse: '%s')".formatted(onTrue, onFalse);
    }
}
