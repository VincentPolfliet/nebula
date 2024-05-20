package dev.vinpol.nebula.dragonship.bigbrain;

import dev.vinpol.nebula.dragonship.shared.storage.CacheStorage;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.models.Agent;
import dev.vinpol.spacetraders.sdk.models.GetAgents200Response;
import dev.vinpol.spacetraders.sdk.models.GetMyAgent200Response;
import org.cache2k.Cache2kBuilder;

import java.time.Duration;

public class AgentsApiCache implements AgentsApi {

    private final AgentsApi agentsApi;
    private final AgentsCache agentsCache = new AgentsCache();

    public AgentsApiCache(AgentsApi agentsApi) {
        this.agentsApi = agentsApi;
    }

    @Override
    public GetMyAgent200Response getAgent(String agentSymbol) {
        if (agentsCache.hasItem(agentSymbol)) {
            return new GetMyAgent200Response()
                .data(agentsCache.get(agentSymbol));
        }

        var response = agentsApi.getAgent(agentSymbol);
        agentsCache.set(agentSymbol, response.getData());
        return response;
    }

    @Override
    public GetAgents200Response getAgents(Integer page, Integer limit) {
        return agentsApi.getAgents(page, limit);
    }

    @Override
    public GetMyAgent200Response getMyAgent() {
        // don't cache my agents api
        return agentsApi.getMyAgent();
    }

    private static final class AgentsCache extends CacheStorage<String, Agent> {

        private AgentsCache() {
            super(Cache2kBuilder.of(String.class, Agent.class)
                .expireAfterWrite(Duration.ofHours(1))
                .build());
        }
    }
}
