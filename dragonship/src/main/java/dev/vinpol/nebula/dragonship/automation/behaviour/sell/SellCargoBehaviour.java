package dev.vinpol.nebula.dragonship.automation.behaviour.sell;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;

record SellCargoBehaviour(FleetApi fleetApi, TradeSymbol tradeSymbol, int units) implements ShipBehaviour {

    @Override
    public ShipBehaviorResult update(Ship ship) {
        SellCargo201Response response = fleetApi.sellCargo(ship.getSymbol(), new SellCargoRequest()
            .symbol(tradeSymbol)
            .units(units)
        );

        SellCargo201ResponseData data = response.getData();
        ship.setCargo(data.getCargo());

        return ShipBehaviorResult.done();
    }
}
