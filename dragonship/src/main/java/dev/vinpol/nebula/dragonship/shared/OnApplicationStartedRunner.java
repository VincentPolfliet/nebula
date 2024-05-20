package dev.vinpol.nebula.dragonship.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.sdk.NebulaProperties;
import dev.vinpol.nebula.dragonship.shared.kv.KVStore;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.models.GetMyAgent200Response;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OnApplicationStartedRunner implements CommandLineRunner {

    private final NebulaProperties properties;
    private final AgentsApi agentsApi;
    private final KVStore store;

    public OnApplicationStartedRunner(NebulaProperties properties, AgentsApi agentsApi, KVStore store) {
        this.properties = properties;
        this.agentsApi = agentsApi;
        this.store = store;
    }

    @Override
    public void run(String... args) throws Exception {
        GetMyAgent200Response myAgent = agentsApi.getMyAgent();

        store.set("agent", myAgent.getData().getSymbol());
        store.set("token", properties.token());
    }
}
