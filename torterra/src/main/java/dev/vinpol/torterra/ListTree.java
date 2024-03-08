package dev.vinpol.torterra;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ListTree<T> implements Torterra.Tree<T> {

    private int index = 0;
    private final List<Leaf<T>> leaves = new ArrayList<>();


    @Override
    public void add(Leaf<T> leaf) {
        Objects.requireNonNull(leaf, "leaf");

        leaves.add(leaf);
    }

    @Override
    public Optional<Leaf<T>> getFirst() {
        return get(0);
    }

    @Override
    public Optional<Leaf<T>> getLast() {
        return get(leaves.size() - 1);
    }

    @Override
    public Optional<Leaf<T>> get(int index) {
        if (index >= leaves.size() || index < 0) {
            return Optional.empty();
        }

        return Optional.of(leaves.get(index));
    }

    @Override
    public Leaf<T> previous() {
        return get(index - 1).orElse(null);
    }

    @Override
    public void setIndexOn(int index) {
        this.index = index;
    }

    @Override
    public void tick(T instance) {
        current().act(instance);
        index++;
    }

    @Override
    public Leaf<T> next() {
        return get(index + 1).orElse(null);
    }

    @Override
    public Leaf<T> current() {
        return get(index).orElse(null);
    }

    @Override
    public int size() {
        return leaves.size();
    }

    @Override
    public void forEach(Consumer<Leaf<T>> c) {
        leaves.forEach(c);
    }
}
