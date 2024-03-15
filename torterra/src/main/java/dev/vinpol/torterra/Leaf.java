package dev.vinpol.torterra;

@FunctionalInterface
public interface Leaf<T> {
    LeafState act(T instance);

    default boolean isFailSafe() {
        return this instanceof FailSafeLeaf<T>;
    }

    default boolean isIterable() {
        return this instanceof IterableLeaf<T>;
    }
}
