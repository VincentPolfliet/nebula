package dev.vinpol.nebula.dragonship.bigbrain;

import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import org.dizitart.no2.Nitrite;
import org.springframework.scheduling.TaskScheduler;

import java.time.Clock;

public final class ThiccTank implements ApiClient {

    private final ApiClient inner;
    private final Nitrite nitrite;
    private final Clock clock;
    private final TaskScheduler taskExecutor;

    public ThiccTank(ApiClient inner, Nitrite nitrite, Clock clock, TaskScheduler taskExecutor) {
        this.inner = inner;
        this.nitrite = nitrite;
        this.clock = clock;
        this.taskExecutor = taskExecutor;
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
        return new SystemsApiCache(inner.systemsApi(), nitrite, clock, taskExecutor);
    }

    @Override
    public ContractsApi contractsApi() {
        return inner.contractsApi();
    }
}
