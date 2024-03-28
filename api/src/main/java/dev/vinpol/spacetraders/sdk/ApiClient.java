package dev.vinpol.spacetraders.sdk;

import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;

public interface ApiClient {
    AgentsApi agentsApi();

    FleetApi fleetApi();

    SystemsApi systemsApi();

    ContractsApi contractsApi();
}
