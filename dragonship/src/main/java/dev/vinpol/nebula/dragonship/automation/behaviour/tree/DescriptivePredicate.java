package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import java.util.Objects;
import java.util.function.Predicate;

public class DescriptivePredicate<T> implements Predicate<T> {

    private final String description;
    private final Predicate<T> predicate;

    public DescriptivePredicate(String description, Predicate<T> predicate) {
        this.description = Objects.requireNonNull(description);
        this.predicate = Objects.requireNonNull(predicate);
    }

    // 🗣
    public static <T> Predicate<T> yapping️(String description, Predicate<T> predicate) {
        return new DescriptivePredicate<>(description, predicate);
    }

    @Override
    public boolean test(T t) {
        return predicate.test(t);
    }

    @Override
    public String toString() {
        return "test(\"%s\")".formatted(description);
    }
}
