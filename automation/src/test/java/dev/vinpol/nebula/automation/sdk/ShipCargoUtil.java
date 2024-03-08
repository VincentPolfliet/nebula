package dev.vinpol.nebula.automation.sdk;

import dev.vinpol.spacetraders.sdk.models.ShipCargo;
import dev.vinpol.spacetraders.sdk.models.ShipCargoItem;
import dev.vinpol.spacetraders.sdk.models.TradeSymbol;

import java.util.Objects;

public class ShipCargoUtil {

    private ShipCargoUtil() {

    }

    public static ShipCargo cargo(int capacity, int units) {
        return new ShipCargo()
                .capacity(capacity)
                .units(units);
    }

    public static ShipCargoItem cargoItem(TradeSymbol tradeSymbol) {
        Objects.requireNonNull(tradeSymbol);

        return new ShipCargoItem()
                .symbol(tradeSymbol)
                .name(tradeSymbol.name());
    }
}
