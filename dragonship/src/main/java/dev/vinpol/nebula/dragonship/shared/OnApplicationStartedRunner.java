package dev.vinpol.nebula.dragonship.shared;

import dev.vinpol.nebula.dragonship.sdk.NebulaProperties;
import dev.vinpol.nebula.dragonship.shared.autoregister.AutoRegisterAction;
import dev.vinpol.nebula.dragonship.shared.kv.KVStore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "nebula.st", name = "ext.on-boot", havingValue = "true")
public class OnApplicationStartedRunner implements CommandLineRunner {

    private final NebulaProperties properties;
    private final AutoRegisterAction autoRegisterAction;
    private final KVStore store;

    public OnApplicationStartedRunner(NebulaProperties properties, AutoRegisterAction autoRegisterAction, KVStore store) {
        this.properties = properties;
        this.autoRegisterAction = autoRegisterAction;
        this.store = store;
    }

    @Override
    public void run(String... args) throws Exception {
        String token = properties.token();

        if (token == null || token.isBlank()) {
            autoRegisterAction.register(properties.autoRegisterSymbol());
        } else {
            store.set("token", properties.token());
        }
    }
}
