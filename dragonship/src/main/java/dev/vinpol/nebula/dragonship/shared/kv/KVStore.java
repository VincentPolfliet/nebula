package dev.vinpol.nebula.dragonship.shared.kv;

public interface KVStore {
    String get(String key);

    void set(String key, String value);
}
