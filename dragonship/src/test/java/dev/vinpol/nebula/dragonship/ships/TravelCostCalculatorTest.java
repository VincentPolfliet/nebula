package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelCostCalculatorTest {

    TravelCostCalculator calculator = spy(new TravelCostCalculator());

    @Test
    public void testSelectBestFlightMode() {
        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>();
        fuelCost.put(ShipNavFlightMode.DRIFT, 100L);
        fuelCost.put(ShipNavFlightMode.STEALTH, 200L);
        fuelCost.put(ShipNavFlightMode.CRUISE, 50L);
        fuelCost.put(ShipNavFlightMode.BURN, 20L);

        ShipNavFlightMode bestMode = calculator.selectBestFlightModeByFuel(fuelCost);
        assertThat(bestMode).isEqualTo(ShipNavFlightMode.BURN);
    }

    @Test
    public void testSelectBestFlightModeEqualCosts() {
        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>();
        fuelCost.put(ShipNavFlightMode.DRIFT, 100L);
        fuelCost.put(ShipNavFlightMode.STEALTH, 100L);
        fuelCost.put(ShipNavFlightMode.CRUISE, 100L);
        fuelCost.put(ShipNavFlightMode.BURN, 100L);

        ShipNavFlightMode bestMode = calculator.selectBestFlightModeByFuel(fuelCost);
        assertThat(bestMode).isEqualTo(ShipNavFlightMode.BURN);
    }

    @Test
    public void testSelectBestFlightModeExtremeValues() {
        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>();
        fuelCost.put(ShipNavFlightMode.DRIFT, 1L);
        fuelCost.put(ShipNavFlightMode.STEALTH, 1L);
        fuelCost.put(ShipNavFlightMode.CRUISE, 1L);
        fuelCost.put(ShipNavFlightMode.BURN, 1L);

        ShipNavFlightMode bestMode = calculator.selectBestFlightModeByFuel(fuelCost);

        assertThat(bestMode).isEqualTo(ShipNavFlightMode.DRIFT);
    }
}
