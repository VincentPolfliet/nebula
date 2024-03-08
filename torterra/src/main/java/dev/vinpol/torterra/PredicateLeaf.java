package dev.vinpol.torterra;

import java.util.function.Predicate;

import static dev.vinpol.torterra.TorterraUtils.transferState;

class PredicateLeaf<T> extends Leaf<T> {

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
    public void act(T instance) {
        Leaf<T> leafToExecute = null;

        if (predicate.test(instance)) {
            leafToExecute = onTrue;
        } else {
            leafToExecute = onFalse;
        }

        leafToExecute.act(instance);
        transferState(leafToExecute, this);
    }
}
