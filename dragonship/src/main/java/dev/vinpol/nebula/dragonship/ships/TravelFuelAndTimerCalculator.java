package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.ToLongFunction;

@Component
public class TravelFuelAndTimerCalculator {

    private final Map<ShipNavFlightMode, ToLongFunction<Double>> FUEL_TRANSFORMERS = new EnumMap<>(ShipNavFlightMode.class);
    private final Map<ShipNavFlightMode, Double> TIME_MULTIPLIER = new EnumMap<>(ShipNavFlightMode.class);

    public TravelFuelAndTimerCalculator() {
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.CRUISE, Math::round);
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.DRIFT, input -> 1);
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.BURN, input -> 2 * Math.round(input));
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.STEALTH, Math::round);

        TIME_MULTIPLIER.put(ShipNavFlightMode.CRUISE, 25d);
        TIME_MULTIPLIER.put(ShipNavFlightMode.DRIFT, 250d);
        TIME_MULTIPLIER.put(ShipNavFlightMode.BURN, 12.5d);
        TIME_MULTIPLIER.put(ShipNavFlightMode.STEALTH, 30d);
    }

    public Map<ShipNavFlightMode, Long> calculateFuel(Coordinate origin, Coordinate destination) {
        Map<ShipNavFlightMode, Long> cost = new HashMap<>();

        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            cost.put(mode, calculateFuel(origin, destination, mode));
        }

        return cost;
    }

    public Map<ShipNavFlightMode, Double> calculateTime(Coordinate origin, Coordinate destination, long engineSpeed) {
        Map<ShipNavFlightMode, Double> cost = new HashMap<>();

        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            Double multiplier = TIME_MULTIPLIER.get(mode);

            double time = Math.round(Math.round(Math.max(1, calculateDistance(origin, destination))) * (multiplier / engineSpeed) + 15);
            cost.put(mode, time);
        }

        return cost;
    }

    public long calculateFuel(Coordinate origin, Coordinate destination, ShipNavFlightMode flightMode) {
        Objects.requireNonNull(origin);
        Objects.requireNonNull(destination);

        double distance = calculateDistance(origin, destination);
        return calculateFuel(distance, flightMode);
    }

    public double calculateDistance(Coordinate origin, Coordinate destination) {
        double ac = Math.abs(origin.y() - destination.y());
        double cb = Math.abs(origin.x() - destination.x());

        return Math.hypot(ac, cb);
    }

    public long calculateFuel(double distance, ShipNavFlightMode flightMode) {
        return FUEL_TRANSFORMERS.get(flightMode).applyAsLong(distance);
    }
}
