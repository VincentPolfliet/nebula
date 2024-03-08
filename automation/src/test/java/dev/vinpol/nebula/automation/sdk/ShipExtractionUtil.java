package dev.vinpol.nebula.automation.sdk;

import dev.vinpol.spacetraders.sdk.models.Extraction;
import dev.vinpol.spacetraders.sdk.models.ExtractionYield;
import dev.vinpol.spacetraders.sdk.models.TradeSymbol;

public class ShipExtractionUtil {

    public static Extraction extraction(String shipSymbol, ExtractionYield extractionYield) {
        return new Extraction()
                .yield(extractionYield)
                .shipSymbol(shipSymbol);
    }

    public static ExtractionYield extractionYield(TradeSymbol symbol, Integer units) {
        return new ExtractionYield()
                .symbol(symbol)
                .units(units);
    }

}


