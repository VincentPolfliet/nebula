package dev.vinpol.nebula.dragonship.automation.sdk;

import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;

import static org.mockito.Mockito.mock;

public class ApiClientStub implements ApiClient {

    private final FleetApi fleets;
    private final SystemsApi systems;
    private final ContractsApi contracts;
    private final AgentsApi agents;

    public ApiClientStub() {
        fleets = mock(FleetApi.class);
        systems = mock(SystemsApi.class);
        contracts = mock(ContractsApi.class);
        agents = mock(AgentsApi.class);
    }

    @Override
    public AgentsApi agentsApi() {
        return agents;
    }

    @Override
    public FleetApi fleetApi() {
        return fleets;
    }

    @Override
    public SystemsApi systemsApi() {
        return systems;
    }

    @Override
    public ContractsApi contractsApi() {
        return contracts;
    }
}
