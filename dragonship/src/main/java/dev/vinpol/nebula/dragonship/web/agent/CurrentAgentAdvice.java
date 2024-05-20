package dev.vinpol.nebula.dragonship.web.agent;

import dev.vinpol.nebula.dragonship.shared.kv.KVStorage;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.models.Agent;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentAgentAdvice {

    private static final ThreadLocal<Agent> CURRENT_AGENT = new ThreadLocal<>();

    private final KVStorage kvStorage;
    private final AgentsApi agentsApi;

    public CurrentAgentAdvice(KVStorage kvStorage, AgentsApi agentsApi) {
        this.kvStorage = kvStorage;
        this.agentsApi = agentsApi;
    }

    @ModelAttribute("agent")
    public Agent agent() {
        String agentSymbol = kvStorage.get("agent");
        Agent currentAgent = agentsApi.getAgent(agentSymbol).getData();
        CURRENT_AGENT.set(currentAgent);
        return currentAgent;
    }

    public static Agent getCurrentAgent() {
        return CURRENT_AGENT.get();
    }
}
