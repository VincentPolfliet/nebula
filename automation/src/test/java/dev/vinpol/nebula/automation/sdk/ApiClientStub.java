package dev.vinpol.nebula.automation.sdk;

import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;

import static org.mockito.Mockito.mock;

public class ApiClientStub implements ApiClient {

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;

    public ApiClientStub() {
        fleetApi = mock(FleetApi.class);
        systemsApi = mock(SystemsApi.class);
    }

    @Override
    public FleetApi fleetApi() {
        return fleetApi;
    }

    @Override
    public SystemsApi systemsApi() {
        return systemsApi;
    }
}
