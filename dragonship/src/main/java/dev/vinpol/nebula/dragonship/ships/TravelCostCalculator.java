package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.dragonship.geo.GridXY;
import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import static dev.vinpol.nebula.dragonship.geo.GridXY.toCoordinate;

@Component
public class TravelCostCalculator {

    public static final Map<ShipNavFlightMode, ToLongFunction<Double>> FUEL_TRANSFORMERS = new EnumMap<>(ShipNavFlightMode.class);
    public static final Map<ShipNavFlightMode, Double> TIME_MULTIPLIER = new EnumMap<>(ShipNavFlightMode.class);

    static {
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.CRUISE, Math::round);
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.DRIFT, _ -> 1L);
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.BURN, input -> 2L * Math.round(input));
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.STEALTH, Math::round);

        TIME_MULTIPLIER.put(ShipNavFlightMode.CRUISE, 25d);
        TIME_MULTIPLIER.put(ShipNavFlightMode.DRIFT, 250d);
        TIME_MULTIPLIER.put(ShipNavFlightMode.BURN, 12.5d);
        TIME_MULTIPLIER.put(ShipNavFlightMode.STEALTH, 30d);
    }

    public TravelCostCalculator() {
    }

    public ShipNavFlightMode selectBestFlightModeByFuel(Map<ShipNavFlightMode, Long> fuelCostPerFlightMode) {
        // Find min and max values for normalization
        long minFuelCost = !fuelCostPerFlightMode.isEmpty() ? Collections.min(fuelCostPerFlightMode.values()) : 0;
        long maxFuelCost = !fuelCostPerFlightMode.isEmpty() ? Collections.max(fuelCostPerFlightMode.values()) : 0;

        Map<ShipNavFlightMode, Double> scores = new HashMap<>();

        // Calculate normalized scores
        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            if (fuelCostPerFlightMode.containsKey(mode)) {
                double fuelCost = (maxFuelCost == minFuelCost) ? 0 : (double) (fuelCostPerFlightMode.get(mode) - minFuelCost) / (maxFuelCost - minFuelCost);
                scores.put(mode, fuelCost);
            }
        }

        // Find the flight mode with the lowest score, else default to DRIFT, because that uses 1 fuel
        return scores.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(ShipNavFlightMode.DRIFT);
    }


    public ShipNavFlightMode selectBestFlightModeByDuration(Map<ShipNavFlightMode, Duration> durationsPerFlightMode) {
        // Find min and max values for normalization
        Duration minDurationCost = !durationsPerFlightMode.isEmpty() ? Collections.min(durationsPerFlightMode.values()) : Duration.ZERO;
        Duration maxDurationCost = !durationsPerFlightMode.isEmpty() ? Collections.max(durationsPerFlightMode.values()) : Duration.ZERO;

        Map<ShipNavFlightMode, Duration> scores = new HashMap<>();

        // Calculate normalized scores
        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            if (durationsPerFlightMode.containsKey(mode)) {
                Duration duration = durationsPerFlightMode.get(mode);

                Duration normalizedDuration = duration.minus(minDurationCost);
                Duration minus = maxDurationCost.minus(minDurationCost);

                Duration normalizedDurationScore = (maxDurationCost == minDurationCost) ? Duration.ZERO : normalizedDuration.dividedBy(minus.toSeconds());
                scores.put(mode, normalizedDurationScore);
            }
        }

        // Find the flight mode with the lowest score, else default to DRIFT, because that uses 1 fuel
        return scores.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(ShipNavFlightMode.DRIFT);
    }

    public ShipNavFlightMode selectBestFlightMode(Waypoint originWaypoint, Waypoint targetWaypoint) {
        GridXY origin = toCoordinate(originWaypoint);
        GridXY target = toCoordinate(targetWaypoint);

        Map<ShipNavFlightMode, Long> fuelCost = calculateFuel(origin, target);
        return selectBestFlightModeByFuel(fuelCost);
    }

    public Map<ShipNavFlightMode, Long> calculateFuel(GridXY origin, GridXY destination) {
        Map<ShipNavFlightMode, Long> cost = new HashMap<>();

        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            cost.put(mode, calculateFuel(origin, destination, mode));
        }

        return cost;
    }


    public Map<ShipNavFlightMode, Duration> calculateTime(GridXY origin, GridXY destination, long engineSpeed) {
        Map<ShipNavFlightMode, Duration> cost = new HashMap<>();

        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            Double multiplier = TIME_MULTIPLIER.get(mode);

            double time = Math.round(Math.round(Math.max(1, calculateDistance(origin, destination))) * (multiplier / engineSpeed) + 15);
            cost.put(mode, Duration.ofSeconds(Math.round(time)));
        }

        return cost;
    }

    public long calculateFuel(GridXY origin, GridXY destination, ShipNavFlightMode flightMode) {
        Objects.requireNonNull(origin);
        Objects.requireNonNull(destination);

        double calculateDistance = calculateDistance(origin, destination);
        return calculateFuel(calculateDistance != 0 ? calculateDistance : 1, flightMode);
    }

    public double calculateDistance(GridXY origin, GridXY destination) {
        double ac = Math.abs(origin.y() - destination.y());
        double cb = Math.abs(origin.x() - destination.x());

        return Math.hypot(ac, cb);
    }

    public long calculateFuel(double distance, ShipNavFlightMode flightMode) {
        return FUEL_TRANSFORMERS.get(flightMode).applyAsLong(distance);
    }

    public Duration calculateTime(GridXY originCoord, GridXY targetCoord, int engineSpeed, ShipNavFlightMode mode) {
        return calculateTime(originCoord, targetCoord, engineSpeed).get(mode);
    }
}
