package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.spacetraders.sdk.models.*;
import org.springframework.stereotype.Component;

import java.lang.System;
import java.util.*;
import java.util.function.ToLongFunction;

import static dev.vinpol.nebula.dragonship.geo.Coordinate.toCoordinate;

@Component
public class TravelFuelAndTimerCalculator {

    public final Map<ShipNavFlightMode, ToLongFunction<Double>> FUEL_TRANSFORMERS = new EnumMap<>(ShipNavFlightMode.class);
    public final Map<ShipNavFlightMode, Double> TIME_MULTIPLIER = new EnumMap<>(ShipNavFlightMode.class);

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

    public ShipNavFlightMode selectBestFlightMode(Map<ShipNavFlightMode, Double> timeCost, Map<ShipNavFlightMode, Long> fuelCost) {
        // Find min and max values for normalization
        double minTimeCost = !timeCost.isEmpty() ? Collections.min(timeCost.values()) : 0;
        double maxTimeCost = !timeCost.isEmpty() ? Collections.max(timeCost.values()) : 0;
        long minFuelCost = !fuelCost.isEmpty() ? Collections.min(fuelCost.values()) : 0;
        long maxFuelCost = !fuelCost.isEmpty() ? Collections.max(fuelCost.values()) : 0;

        // Weights for time cost and fuel cost
        double timeWeight = 0.7;
        double fuelWeight = 0.3;

        Map<ShipNavFlightMode, Double> scores = new HashMap<>();

        // Calculate normalized scores
        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            if (fuelCost.containsKey(mode) && timeCost.containsKey(mode)) {
                double normalizedTimeCost = (maxTimeCost == minTimeCost) ? 0 : (timeCost.get(mode) - minTimeCost) / (maxTimeCost - minTimeCost);
                double normalizedFuelCost = (maxFuelCost == minFuelCost) ? 0 : (double) (fuelCost.get(mode) - minFuelCost) / (maxFuelCost - minFuelCost);

                double score = timeWeight * normalizedTimeCost + fuelWeight * normalizedFuelCost;
                scores.put(mode, score);
            }
        }

        // Find the flight mode with the lowest score, else default to DRIFT, because that uses 1 fuel
        return scores.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(ShipNavFlightMode.DRIFT);
    }

    public ShipNavFlightMode selectBestFlightMode(ShipEngine shipEngine, Waypoint originWaypoint, Waypoint targetWaypoint) {
        Coordinate origin = toCoordinate(originWaypoint);
        Coordinate target = toCoordinate(targetWaypoint);

        Map<ShipNavFlightMode, Double> timeCost = calculateTime(origin, target, shipEngine.getSpeed());
        Map<ShipNavFlightMode, Long> fuelCost = calculateFuel(origin, target);

        return selectBestFlightMode(timeCost, fuelCost);
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
