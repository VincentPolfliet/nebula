package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.dragonship.geo.GridXY;
import dev.vinpol.spacetraders.sdk.models.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.ToLongFunction;

import static dev.vinpol.nebula.dragonship.geo.GridXY.toCoordinate;

@Component
public class TravelCostCalculator {

    public static final Map<ShipNavFlightMode, ToLongFunction<Double>> FUEL_TRANSFORMERS = new EnumMap<>(ShipNavFlightMode.class);
    public static final Map<ShipNavFlightMode, Double> TIME_MULTIPLIER = new EnumMap<>(ShipNavFlightMode.class);

    static {
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.CRUISE, Math::round);
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.DRIFT, _ -> 1);
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.BURN, input -> 2 * Math.round(input));
        FUEL_TRANSFORMERS.put(ShipNavFlightMode.STEALTH, Math::round);

        TIME_MULTIPLIER.put(ShipNavFlightMode.CRUISE, 25d);
        TIME_MULTIPLIER.put(ShipNavFlightMode.DRIFT, 250d);
        TIME_MULTIPLIER.put(ShipNavFlightMode.BURN, 12.5d);
        TIME_MULTIPLIER.put(ShipNavFlightMode.STEALTH, 30d);
    }

    public TravelCostCalculator() {
    }

    public ShipNavFlightMode selectBestFlightMode(Map<ShipNavFlightMode, Long> fuelCostPerFlightMode) {
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
        return scores.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(ShipNavFlightMode.DRIFT);
    }

    public ShipNavFlightMode selectBestFlightMode(Waypoint originWaypoint, Waypoint targetWaypoint) {
        GridXY origin = toCoordinate(originWaypoint);
        GridXY target = toCoordinate(targetWaypoint);

        Map<ShipNavFlightMode, Long> fuelCost = calculateFuel(origin, target);
        return selectBestFlightMode(fuelCost);
    }

    public Map<ShipNavFlightMode, Long> calculateFuel(GridXY origin, GridXY destination) {
        Map<ShipNavFlightMode, Long> cost = new HashMap<>();

        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            cost.put(mode, calculateFuel(origin, destination, mode));
        }

        return cost;
    }


    public Map<ShipNavFlightMode, Double> calculateTime(GridXY origin, GridXY destination, long engineSpeed) {
        Map<ShipNavFlightMode, Double> cost = new HashMap<>();

        for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
            Double multiplier = TIME_MULTIPLIER.get(mode);

            double time = Math.round(Math.round(Math.max(1, calculateDistance(origin, destination))) * (multiplier / engineSpeed) + 15);
            cost.put(mode, time);
        }

        return cost;
    }

    public long calculateFuel(GridXY origin, GridXY destination, ShipNavFlightMode flightMode) {
        Objects.requireNonNull(origin);
        Objects.requireNonNull(destination);

        double distance = calculateDistance(origin, destination);
        return calculateFuel(distance, flightMode);
    }

    public double calculateDistance(GridXY origin, GridXY destination) {
        double ac = Math.abs(origin.y() - destination.y());
        double cb = Math.abs(origin.x() - destination.x());

        return Math.hypot(ac, cb);
    }

    public long calculateFuel(double distance, ShipNavFlightMode flightMode) {
        return FUEL_TRANSFORMERS.get(flightMode).applyAsLong(distance);
    }

    public double calculateTime(GridXY originCoord, GridXY targetCoord, int engineSpeed, ShipNavFlightMode mode) {
        return calculateTime(originCoord, targetCoord, engineSpeed).get(mode);
    }

    public static double approximationDistance(long fuel, double time, int engineSpeed, ShipNavFlightMode flightMode) {
        Double timeMultiplier = TIME_MULTIPLIER.get(flightMode);

        if (timeMultiplier == null) {
            throw new IllegalArgumentException("Invalid flight mode provided.");
        }

        // Calculate the approximate distance using the time formula: time = distance * (timeMultiplier / engineSpeed) + 15
        double distanceFromTime = (time - 15) * engineSpeed / timeMultiplier;

        // Calculate the distance using the fuel formula: fuel = FUEL_TRANSFORMERS.get(flightMode).applyAsLong(distance)
        ToLongFunction<Double> fuelTransformer = FUEL_TRANSFORMERS.get(flightMode);
        if (fuelTransformer == null) {
            throw new IllegalArgumentException("Invalid flight mode provided.");
        }

        // Here we assume the reverse of the fuel transformer as linear approximation
        // For more complex transformers, a more sophisticated approach may be needed
        double distanceFromFuel = getDistanceFromFuel(fuel, flightMode);

        // Average the two distances as a simple method to estimate the actual distance
        return (distanceFromTime + distanceFromFuel) / 2.0;
    }

    private static double getDistanceFromFuel(long fuel, ShipNavFlightMode flightMode) {
        double distanceFromFuel = 0;
        if (flightMode == ShipNavFlightMode.DRIFT) {
            distanceFromFuel = fuel;
        } else if (flightMode == ShipNavFlightMode.BURN) {
            distanceFromFuel = fuel / 2.0;
        } else {
            distanceFromFuel = fuel; // This is an approximation since Math.round() is not easily reversible
        }
        return distanceFromFuel;
    }
}
