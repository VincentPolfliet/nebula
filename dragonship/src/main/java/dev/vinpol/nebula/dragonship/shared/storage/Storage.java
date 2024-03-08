package dev.vinpol.nebula.dragonship.shared.storage;

import java.util.stream.Stream;

public interface Storage<K, T> {

    void store(K key, T item);

    T retrieve(K key);

    Stream<T> stream();
}