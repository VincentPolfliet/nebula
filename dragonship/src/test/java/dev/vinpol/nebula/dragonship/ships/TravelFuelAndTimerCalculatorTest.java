package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.spacetraders.sdk.models.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelFuelAndTimerCalculatorTest {

    TravelFuelAndTimerCalculator calculator = spy(new TravelFuelAndTimerCalculator());

    @Test
    public void testSelectBestFlightMode() {
        Map<ShipNavFlightMode, Double> timeCost = new HashMap<>();
        timeCost.put(ShipNavFlightMode.DRIFT, 10.0);
        timeCost.put(ShipNavFlightMode.STEALTH, 20.0);
        timeCost.put(ShipNavFlightMode.CRUISE, 5.0);
        timeCost.put(ShipNavFlightMode.BURN, 2.0);

        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>();
        fuelCost.put(ShipNavFlightMode.DRIFT, 100L);
        fuelCost.put(ShipNavFlightMode.STEALTH, 200L);
        fuelCost.put(ShipNavFlightMode.CRUISE, 50L);
        fuelCost.put(ShipNavFlightMode.BURN, 20L);

        ShipNavFlightMode bestMode = calculator.selectBestFlightMode(timeCost, fuelCost);
        assertThat(bestMode).isEqualTo(ShipNavFlightMode.BURN);
    }

    @Test
    public void testSelectBestFlightModeDifferentWeights() {
        // Different set of mocked data
        Map<ShipNavFlightMode, Double> timeCost = new HashMap<>();
        timeCost.put(ShipNavFlightMode.DRIFT, 10.0);
        timeCost.put(ShipNavFlightMode.STEALTH, 15.0);
        timeCost.put(ShipNavFlightMode.CRUISE, 7.0);
        timeCost.put(ShipNavFlightMode.BURN, 3.0);

        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>();
        fuelCost.put(ShipNavFlightMode.DRIFT, 80L);
        fuelCost.put(ShipNavFlightMode.STEALTH, 60L);
        fuelCost.put(ShipNavFlightMode.CRUISE, 30L);
        fuelCost.put(ShipNavFlightMode.BURN, 10L);

        ShipNavFlightMode bestMode = calculator.selectBestFlightMode(timeCost, fuelCost);
        assertThat(bestMode).isEqualTo(ShipNavFlightMode.BURN);
    }

    @Test
    public void testSelectBestFlightModeEqualCosts() {
        Map<ShipNavFlightMode, Double> timeCost = new HashMap<>();
        timeCost.put(ShipNavFlightMode.DRIFT, 10.0);
        timeCost.put(ShipNavFlightMode.STEALTH, 10.0);
        timeCost.put(ShipNavFlightMode.CRUISE, 10.0);
        timeCost.put(ShipNavFlightMode.BURN, 10.0);

        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>();
        fuelCost.put(ShipNavFlightMode.DRIFT, 100L);
        fuelCost.put(ShipNavFlightMode.STEALTH, 100L);
        fuelCost.put(ShipNavFlightMode.CRUISE, 100L);
        fuelCost.put(ShipNavFlightMode.BURN, 100L);

        ShipNavFlightMode bestMode = calculator.selectBestFlightMode(timeCost, fuelCost);
        assertThat(bestMode).isNotNull();
    }

    @Test
    public void testSelectBestFlightModeFuelCostMoreImportant() {
        Map<ShipNavFlightMode, Double> timeCost = new HashMap<>();
        timeCost.put(ShipNavFlightMode.DRIFT, 10.0);
        timeCost.put(ShipNavFlightMode.STEALTH, 10.0);
        timeCost.put(ShipNavFlightMode.CRUISE, 10.0);
        timeCost.put(ShipNavFlightMode.BURN, 10.0);

        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>();
        fuelCost.put(ShipNavFlightMode.DRIFT, 100L);
        fuelCost.put(ShipNavFlightMode.STEALTH, 50L);
        fuelCost.put(ShipNavFlightMode.CRUISE, 20L);
        fuelCost.put(ShipNavFlightMode.BURN, 10L);

        ShipNavFlightMode bestMode = calculator.selectBestFlightMode(timeCost, fuelCost);

        assertThat(bestMode).isEqualTo(ShipNavFlightMode.BURN);
    }

    @Test
    public void testSelectBestFlightModeTimeCostDominates() {
        Map<ShipNavFlightMode, Double> timeCost = new HashMap<>();
        timeCost.put(ShipNavFlightMode.DRIFT, 20.0);
        timeCost.put(ShipNavFlightMode.STEALTH, 15.0);
        timeCost.put(ShipNavFlightMode.CRUISE, 10.0);
        timeCost.put(ShipNavFlightMode.BURN, 5.0);

        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>();
        fuelCost.put(ShipNavFlightMode.DRIFT, 50L);
        fuelCost.put(ShipNavFlightMode.STEALTH, 40L);
        fuelCost.put(ShipNavFlightMode.CRUISE, 30L);
        fuelCost.put(ShipNavFlightMode.BURN, 20L);

        ShipNavFlightMode bestMode = calculator.selectBestFlightMode(timeCost, fuelCost);

        assertThat(bestMode).isEqualTo(ShipNavFlightMode.BURN);
    }

    @Test
    public void testSelectBestFlightModeExtremeValues() {
        Map<ShipNavFlightMode, Double> timeCost = new HashMap<>();
        timeCost.put(ShipNavFlightMode.DRIFT, 1000.0);
        timeCost.put(ShipNavFlightMode.STEALTH, 1.0);
        timeCost.put(ShipNavFlightMode.CRUISE, 1000.0);
        timeCost.put(ShipNavFlightMode.BURN, 1000.0);

        Map<ShipNavFlightMode, Long> fuelCost = new HashMap<>();
        fuelCost.put(ShipNavFlightMode.DRIFT, 10L);
        fuelCost.put(ShipNavFlightMode.STEALTH, 10L);
        fuelCost.put(ShipNavFlightMode.CRUISE, 10L);
        fuelCost.put(ShipNavFlightMode.BURN, 10L);

        ShipNavFlightMode bestMode = calculator.selectBestFlightMode(timeCost, fuelCost);

        assertThat(bestMode).isEqualTo(ShipNavFlightMode.STEALTH);
    }
}
