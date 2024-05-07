package dev.vinpol.nebula.dragonship.shared.kv;

import dev.vinpol.nebula.dragonship.shared.storage.CacheStorage;
import org.springframework.stereotype.Component;

@Component
public class KVStorage extends CacheStorage<String, String> implements KVStore {

    protected KVStorage() {
        super(String.class, String.class);
    }

    @Override
    public String get(String key) {
        return retrieve(key);
    }

    @Override
    public void set(String key, String value) {
        store(key, value);
    }
}
