package dev.vinpol.nebula.dragonship.shared;

import dev.vinpol.nebula.dragonship.sdk.NebulaProperties;
import dev.vinpol.nebula.dragonship.shared.kv.KVStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "nebula.st", name = "ext.on-boot", havingValue = "true")
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
