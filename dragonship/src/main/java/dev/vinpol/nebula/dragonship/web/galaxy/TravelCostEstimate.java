package dev.vinpol.nebula.dragonship.web.galaxy;

import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;

import java.time.Duration;

public record TravelCostEstimate(
    ShipNavFlightMode mode,
    long fuelCost,
    Duration duration,
    Long remainingFuel,
    boolean isSuggested
) {
    public boolean hasRemainingFuel() {
        return remainingFuel != null && remainingFuel > 0;
    }
}
