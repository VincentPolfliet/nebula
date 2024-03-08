package dev.vinpol.spacetraders.sdk;

import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;

public interface ApiClient {
    FleetApi fleetApi();

    SystemsApi systemsApi();
}
