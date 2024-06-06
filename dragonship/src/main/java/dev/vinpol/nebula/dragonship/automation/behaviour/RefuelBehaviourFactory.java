package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;

public class RefuelBehaviourFactory implements ShipBehaviourFactory {

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;

    public RefuelBehaviourFactory(FleetApi fleetApi, SystemsApi systemsApi) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
    }

    @Override
    public ShipBehaviour create() {
        return new RefuelShipBehaviour(fleetApi, systemsApi);
    }

}
