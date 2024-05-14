package dev.vinpol.nebula.dragonship.shared.kv;

import com.fasterxml.jackson.annotation.JsonProperty;
import one.util.streamex.StreamEx;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.repository.ObjectRepository;
import org.dizitart.no2.repository.annotations.Id;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.dizitart.no2.filters.FluentFilter.where;

@Component
public class KVStorage implements KVStore {

    private final ObjectRepository<KeyValue> repository;

    protected KVStorage(Nitrite nitrite) {
        this.repository = nitrite.getRepository(KeyValue.class);
    }

    @Override
    public String get(String key) {
        Objects.requireNonNull(key);

        return StreamEx.of(repository.find(where("key").eq(key)).iterator())
                .findFirst()
                .map(KeyValue::value)
                .orElseThrow();
    }

    @Override
    public void set(String key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        repository.update(new KeyValue(key, value), true);
    }

    private record KeyValue(@Id @JsonProperty("key") String key, @JsonProperty("value") String value) {

    }
}
