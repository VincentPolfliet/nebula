package dev.vinpol.nebula.dragonship.bigbrain;

import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.System;
import dev.vinpol.spacetraders.sdk.models.*;
import org.dizitart.no2.Nitrite;
import retrofit2.Call;

public class SystemsApiCache implements SystemsApi {

    private final SystemsApi systemsApi;
    private final NitriteApiCache<String, System> systemCache;
    private final NitriteApiCache<String, Waypoint> waypointsPerSystemCache;

    public SystemsApiCache(SystemsApi systemsApi, Nitrite nitrite) {
        this.systemsApi = systemsApi;
        this.systemCache = new NitriteApiCache<>(nitrite, System.class, "symbol");
        this.waypointsPerSystemCache = new NitriteApiCache<>(nitrite, Waypoint.class, "symbol");
    }

    @Override
    public GetSystem200Response getSystem(String systemSymbol) {
        return systemCache.getByIdAsOptional(systemSymbol)
            .map(system -> new GetSystem200Response().data(system))
            .orElseGet(() -> {
                GetSystem200Response shipResponse = systemsApi.getSystem(systemSymbol);

                System system = shipResponse.getData();
                systemCache.updateOrInsert(systemSymbol, system);
                return shipResponse;
            });
    }

    @Override
    public GetSystemWaypoints200Response getSystemWaypoints(String systemSymbol, Integer page, Integer limit, WaypointType type, WaypointTraitSymbol... traits) {
        GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(systemSymbol, page, limit, type, traits);

        for (Waypoint waypoint : response.getData()) {
            waypointsPerSystemCache.updateOrInsert(waypoint.getSymbol(), waypoint);
        }

        return response;
    }

    @Override
    public GetSystems200Response getSystems(Integer page, Integer limit) {
        GetSystems200Response response = systemsApi.getSystems(page, limit);

        for (System system : response.getData()) {
            systemCache.updateOrInsert(system.getSymbol(), system);
        }

        return response;
    }

    @Override
    public GetWaypoint200Response getWaypoint(String systemSymbol, String waypointSymbol) {
        return waypointsPerSystemCache.getByIdAsOptional(waypointSymbol)
            .map(waypoint -> new GetWaypoint200Response().data(waypoint))
            .orElseGet(() -> {
                GetWaypoint200Response shipResponse = systemsApi.getWaypoint(systemSymbol, waypointSymbol);
                Waypoint waypoint = shipResponse.getData();
                waypointsPerSystemCache.updateOrInsert(waypointSymbol, waypoint);
                return shipResponse;
            });
    }

    @Override
    public Call<GetConstruction200Response> getConstruction(String systemSymbol, String waypointSymbol) {
        return systemsApi.getConstruction(systemSymbol, waypointSymbol);
    }

    @Override
    public Call<GetJumpGate200Response> getJumpGate(String systemSymbol, String waypointSymbol) {
        return systemsApi.getJumpGate(systemSymbol, waypointSymbol);
    }

    @Override
    public GetMarket200Response getMarket(String systemSymbol, String waypointSymbol) {
        return systemsApi.getMarket(systemSymbol, waypointSymbol);
    }

    @Override
    public Call<GetShipyard200Response> getShipyard(String systemSymbol, String waypointSymbol) {
        return systemsApi.getShipyard(systemSymbol, waypointSymbol);
    }

    @Override
    public Call<SupplyConstruction201Response> supplyConstruction(String systemSymbol, String waypointSymbol, SupplyConstructionRequest supplyConstructionRequest) {
        return systemsApi.supplyConstruction(systemSymbol, waypointSymbol, supplyConstructionRequest);
    }
}
