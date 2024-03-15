package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.RefuelShip200Response;
import dev.vinpol.spacetraders.sdk.models.RefuelShipRequest;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipFuel;

public class RefuelBehaviour implements ShipBehaviourFactory {

    private final FleetApi fleetApi;

    public RefuelBehaviour(FleetApi fleetApi) {
        this.fleetApi = fleetApi;
    }

    @Override
    public ShipBehaviour create() {
        return new ShipBehaviour() {
            @Override
            public String getName() {
                return "refuel";
            }

            @Override
            public ShipBehaviourResult update(Ship ship) {
                if (!ship.isDocked()) {
                    return ShipBehaviourResult.failure(FailureReason.NOT_DOCKED);
                }

                if (ship.isFuelFull()) {
                    return ShipBehaviourResult.failure(FailureReason.FUEL_IS_FULL);
                }

                RefuelShipRequest refuelShipRequest = new RefuelShipRequest()
                    .fromCargo(true) // set true to also include fuel in cargo to refuel
                    .units(null); // null will try to maximize refueling

                RefuelShip200Response refuelResponse = fleetApi.refuelShip(ship.getSymbol(), refuelShipRequest);
                ShipFuel fuel = refuelResponse.getData().getFuel();
                ship.setFuel(fuel);

                return ShipBehaviourResult.done();
            }
        };
    }
}
