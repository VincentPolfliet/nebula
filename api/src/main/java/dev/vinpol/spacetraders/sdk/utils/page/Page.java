package dev.vinpol.spacetraders.sdk.utils.page;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public record Page<T>(Collection<T> data, int total) {
    public static <T> Page<T> of(T data) {
        Objects.requireNonNull(data);

        return ofCollection(Collections.singleton(data));
    }

    public static <T> Page<T> ofCollection(Collection<T> collection) {
        Objects.requireNonNull(collection);

        return new Page<>(collection, collection.size());
    }
}
