package dev.vinpol.torterra;

@FunctionalInterface
public interface Leaf<T> {
    LeafState act(T instance);
}
