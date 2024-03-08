package dev.vinpol.torterra;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Torterra {

    @SafeVarargs
    static <T> Tree<T> plant(Leaf<T>... leaves) {
        ListTree<T> listTree = new ListTree<>();

        for (Leaf<T> leaf : leaves) {
            listTree.add(leaf);
        }

        return listTree;
    }

    static <T> Leaf<T> succeed() {
        return new SucceedLeaf<>();
    }

    static <T> Leaf<T> fail() {
        return new FailLeaf<>();
    }

    static Leaf<Void> runnable(Runnable runnable) {
        return new RunnableLeaf(runnable);
    }

    static <T> Leaf<T> failSafe(Leaf<T> inner) {
        return new FailSafeLeaf<>(inner);
    }

    static <T> Leaf<T> predicate(Predicate<T> predicate, Leaf<T> onTrue, Leaf<T> onFalse) {
        return new PredicateLeaf<>(predicate, onTrue, onFalse);
    }

    static <T> Leaf<T> predicate(Predicate<T> predicate) {
        return new PredicateLeaf<>(predicate, succeed(), fail());
    }


    static <T> Leaf<T> predicate(Predicate<T> predicate, Leaf<T> onTrue) {
        return new PredicateLeaf<>(predicate, onTrue, succeed());
    }

    static <T> Leaf<T> doWhile(Predicate<T> predicate, Leaf<T> leaf) {
        return new DoWhileLeaf<>(predicate, leaf);
    }

    static <T> Leaf<T> sequence(Iterable<Leaf<T>> leaves) {
        return new Sequence<>(leaves);
    }

    static <T> Leaf<T> selector(Iterable<Leaf<T>> leaves) {
        return new Selector<>(leaves);
    }

    @SafeVarargs
    static <T> Leaf<T> selector(Leaf<T>... leaves) {
        return new Selector<>(List.of(leaves));
    }


    @SafeVarargs
    static <T> Leaf<T> safeSequence(Leaf<T>... leaves) {
        return failSafe(sequence(leaves));
    }

    @SafeVarargs
    static <T> Leaf<T> sequence(Leaf<T>... leaves) {
        return new Sequence<>(List.of(leaves));
    }

    interface Tree<T> {

        void add(Leaf<T> leaf);

        Optional<Leaf<T>> getFirst();

        Optional<Leaf<T>> getLast();

        Optional<Leaf<T>> get(int index);

        Leaf<T> previous();

        void setIndexOn(int index);

        void tick(T instance);

        Leaf<T> next();

        default boolean hasNext() {
            return next() != null;
        }

        Leaf<T> current();

        int size();

        void forEach(Consumer<Leaf<T>> t);
    }
}
