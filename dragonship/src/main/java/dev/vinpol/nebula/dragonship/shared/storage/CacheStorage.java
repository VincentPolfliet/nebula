package dev.vinpol.nebula.dragonship.shared.storage;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

import java.util.stream.Stream;

public abstract class CacheStorage<K, T> implements Storage<K, T> {

    protected final Cache<K, T> cache;

    protected CacheStorage(Class<K> keyType, Class<T> valueType) {
        cache = Cache2kBuilder.of(keyType, valueType)
            .eternal(true)
            .build();
    }

    protected CacheStorage(Cache<K, T> cache) {
        this.cache = cache;
    }

    @Override
    public void store(K key, T item) {
        cache.put(key, item);
    }

    @Override
    public T retrieve(K key) {
        return cache.get(key);
    }

    @Override
    public Stream<T> stream() {
        return cache.getAll(cache.keys()).values().stream();
    }
}
