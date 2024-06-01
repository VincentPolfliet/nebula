package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;

public class SellCargoBehaviourFactory implements ShipBehaviourFactory {

    private final FleetApi fleetApi;
    private final TradeSymbol tradeSymbol;
    private final int units;

    public SellCargoBehaviourFactory(FleetApi fleetApi, TradeSymbol tradeSymbol, int units) {
        this.fleetApi = fleetApi;
        this.tradeSymbol = tradeSymbol;
        this.units = units;
    }

    @Override
    public ShipBehaviour create() {
        return new SellCargoBehaviour(fleetApi, tradeSymbol, units);
    }

    private record SellCargoBehaviour(FleetApi fleetApi, TradeSymbol tradeSymbol, int units) implements ShipBehaviour {

        @Override
        public ShipBehaviourResult update(Ship ship) {
            SellCargo201Response response = fleetApi.sellCargo(ship.getSymbol(), new SellCargoRequest()
                .symbol(tradeSymbol)
                .units(units)
            );

            SellCargo201ResponseData data = response.getData();
            ship.setCargo(data.getCargo());

            return ShipBehaviourResult.done();
        }
    }
}
