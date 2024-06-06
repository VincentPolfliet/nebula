package dev.vinpol.nebula.dragonship.bigbrain;

import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import org.dizitart.no2.Nitrite;

public final class ThiccTank implements ApiClient {

    private final ApiClient inner;
    private final Nitrite nitrite;

    public ThiccTank(ApiClient inner, Nitrite nitrite) {
        this.inner = inner;
        this.nitrite = nitrite;
    }

    @Override
    public AgentsApi agentsApi() {
        return new AgentsApiCache(inner.agentsApi());
    }

    @Override
    public FleetApi fleetApi() {
        return inner.fleetApi();
    }

    @Override
    public SystemsApi systemsApi() {
        return new SystemsApiCache(inner.systemsApi(), nitrite);
    }

    @Override
    public ContractsApi contractsApi() {
        return inner.contractsApi();
    }
}
