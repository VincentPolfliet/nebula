package dev.vinpol.nebula.dragonship.sdk;

import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.api.*;

import static org.mockito.Mockito.mock;

public class ApiClientStub implements ApiClient {

    private final DefaultApi defaults = mock(DefaultApi.class);
    private final FleetApi fleets = mock(FleetApi.class);
    private final SystemsApi systems = mock(SystemsApi.class);
    private final ContractsApi contracts = mock(ContractsApi.class);
    private final AgentsApi agents = mock(AgentsApi.class);

    @Override
    public DefaultApi defaults() {
        return defaults;
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
