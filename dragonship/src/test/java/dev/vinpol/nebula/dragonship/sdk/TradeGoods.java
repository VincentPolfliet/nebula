package dev.vinpol.nebula.dragonship.sdk;

import dev.vinpol.spacetraders.sdk.models.TradeGood;
import dev.vinpol.spacetraders.sdk.models.TradeSymbol;

public class TradeGoods {
    private TradeGoods() {

    }

    public static TradeGood fuel() {
        return ofSymbol(TradeSymbol.FUEL);
    }

    public static TradeGood ofSymbol(TradeSymbol symbol) {
        return new TradeGood()
            .name(symbol.name())
            .description(symbol.name())
            .symbol(symbol);
    }
}
