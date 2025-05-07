package dev.vinpol.nebula.dragonship.shared.storage;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

import java.util.stream.Stream;

public abstract class CacheStorage<K, T> implements Storage<K, T> {

    protected final Cache<K, T> cache;

    protected CacheStorage(Class<K> keyType, Class<T> valueType) {
        cache = configure(Cache2kBuilder.of(keyType, valueType));
    }

    protected CacheStorage(Cache<K, T> cache) {
        this.cache = cache;
    }

    protected Cache<K, T> configure(Cache2kBuilder<K, T> cacheBuilder) {
        return cacheBuilder
            .eternal(true)
            .build();
    }

    @Override
    public void set(K key, T item) {
        cache.put(key, item);
    }

    @Override
    public T get(K key) {
        return cache.get(key);
    }

    @Override
    public Stream<T> streamValues() {
        return cache.getAll(cache.keys()).values().stream();
    }
}
