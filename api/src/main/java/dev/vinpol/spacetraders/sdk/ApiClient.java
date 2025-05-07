package dev.vinpol.spacetraders.sdk;

import dev.vinpol.spacetraders.sdk.api.*;

public interface ApiClient {
    DefaultApi defaults();

    AgentsApi agentsApi();

    FleetApi fleetApi();

    SystemsApi systemsApi();

    ContractsApi contractsApi();
}
