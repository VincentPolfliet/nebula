package dev.vinpol.nebula.dragonship.web.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public CurrentAgentAdvice(KVStorage kvStorage, AgentsApi agentsApi, ObjectMapper objectMapper) {
        this.kvStorage = kvStorage;
        this.agentsApi = agentsApi;
        this.objectMapper = objectMapper;
    }

    @ModelAttribute("agent")
    public Agent agent() throws JsonProcessingException {
        if (!kvStorage.contains("agent")) {
            Agent currentAgent = agentsApi.getMyAgent().getData();
            kvStorage.set("agent", objectMapper.writeValueAsString(currentAgent));
            CURRENT_AGENT.set(currentAgent);
        } else {
            String agentJson = kvStorage.get("agent");
            Agent agent = objectMapper.readValue(agentJson, Agent.class);
            CURRENT_AGENT.set(agent);
        }

        return getCurrentAgent();
    }

    public static Agent getCurrentAgent() {
        return CURRENT_AGENT.get();
    }
}
