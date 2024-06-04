package dev.vinpol.nebula.dragonship.automation.behaviour.sell;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
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
}
