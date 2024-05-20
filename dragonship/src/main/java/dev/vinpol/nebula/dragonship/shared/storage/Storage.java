package dev.vinpol.nebula.dragonship.shared.storage;

import java.util.stream.Stream;

public interface Storage<K, T> {

    void set(K key, T item);

    T get(K key);

    default boolean hasItem(K key) {
        return get(key) != null;
    }

    Stream<T> streamValues();
}
