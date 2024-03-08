package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.RefuelShip200Response;
import dev.vinpol.spacetraders.sdk.models.RefuelShipRequest;
import dev.vinpol.spacetraders.sdk.models.ShipFuel;

public class RefuelBehaviour implements ShipBehaviourFactory {

    private final FleetApi fleetApi;

    public RefuelBehaviour(FleetApi fleetApi) {
        this.fleetApi = fleetApi;
    }

    @Override
    public ShipBehaviour create() {
        return (ship) -> {
            if (ship.isFuelFull()) {
                return ShipBehaviourResult.done();
            }

            RefuelShipRequest refuelShipRequest = new RefuelShipRequest()
                .fromCargo(true) // set true to also include fuel in cargo to refuel
                .units(null); // null will try to maximize refueling

            RefuelShip200Response refuelResponse = fleetApi.refuelShip(ship.getSymbol(), refuelShipRequest);
            ShipFuel fuel = refuelResponse.getData().getFuel();
            ship.setFuel(fuel);

            return ShipBehaviourResult.done();
        };
    }
}
