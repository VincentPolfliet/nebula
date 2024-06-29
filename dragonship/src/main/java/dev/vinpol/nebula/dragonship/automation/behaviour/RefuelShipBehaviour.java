package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;

public class RefuelShipBehaviour implements ShipBehaviour {
    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;

    public RefuelShipBehaviour(FleetApi fleetApi, SystemsApi systemsApi) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
    }

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

        String currentSystem = ship.getNav().getSystemSymbol();
        String currentLocationSymbol = ship.getNav().getWaypointSymbol();
        Waypoint waypoint = systemsApi.getWaypoint(currentSystem, currentLocationSymbol).getData();

        if (!isMarketPlace(waypoint)) {
            return ShipBehaviourResult.failure(FailureReason.NOT_AT_MARKET_WAYPOINT);
        }

        Market market = systemsApi.getMarket(currentSystem, currentLocationSymbol).getData();

        if (!sellsFuel(market)) {
            return ShipBehaviourResult.failure(FailureReason.MARKET_DOES_NOT_SELL_FUEL);
        }

        RefuelShipRequest refuelShipRequest = getRefuelRequestForShip(ship);

        RefuelShip200Response refuelResponse = fleetApi.refuelShip(ship.getSymbol(), refuelShipRequest);
        ShipFuel fuel = refuelResponse.getData().getFuel();
        ship.setFuel(fuel);

        return ShipBehaviourResult.done();
    }

    public static RefuelShipRequest getRefuelRequestForShip(Ship ship) {
        RefuelShipRequest refuelShipRequest = new RefuelShipRequest()
            .fromCargo(checkIfCargoHasFuel(ship.getCargo())) // set true to also include fuel in cargo to refuel
            .units(null); // null will try to maximize refueling

        return refuelShipRequest;
    }


    public static boolean sellsFuel(Market market) {
        return market.getExports().stream().anyMatch(tg -> tg.getSymbol() == TradeSymbol.FUEL);
    }

    public static boolean isMarketPlace(Waypoint waypoint) {
        return waypoint.getTraits().stream().anyMatch(t -> t.getSymbol() == WaypointTraitSymbol.MARKETPLACE);
    }

    private static boolean checkIfCargoHasFuel(ShipCargo cargo) {
        return cargo.getInventory().stream().anyMatch(i -> i.getSymbol() == TradeSymbol.FUEL);
    }
}
