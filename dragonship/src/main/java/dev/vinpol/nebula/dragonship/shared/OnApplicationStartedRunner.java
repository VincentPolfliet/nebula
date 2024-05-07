package dev.vinpol.nebula.dragonship.shared;

import dev.vinpol.nebula.dragonship.shared.kv.KVStore;
import dev.vinpol.nebula.dragonship.sdk.NebulaProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OnApplicationStartedRunner implements CommandLineRunner {

    private final NebulaProperties properties;
    private final KVStore store;

    public OnApplicationStartedRunner(NebulaProperties properties, KVStore store) {
        this.properties = properties;
        this.store = store;
    }

    @Override
    public void run(String... args) throws Exception {
        store.set("token", properties.token());
    }
}
