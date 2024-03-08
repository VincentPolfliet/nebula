package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.spacetraders.sdk.models.ShipNavFlightMode;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.ToLongFunction;

@Component
public class FuelCalculator {

    private final Map<ShipNavFlightMode, ToLongFunction<Double>> transformers = new EnumMap<>(ShipNavFlightMode.class);

    public FuelCalculator() {
        transformers.put(ShipNavFlightMode.CRUISE, Math::round);
        transformers.put(ShipNavFlightMode.DRIFT, input -> 1);
        transformers.put(ShipNavFlightMode.BURN, input -> 2 * Math.round(input));
        transformers.put(ShipNavFlightMode.STEALTH, Math::round);
    }

    public long calculate(Coordinate origin, Coordinate destination, ShipNavFlightMode flightMode) {
        Objects.requireNonNull(origin);
        Objects.requireNonNull(destination);

        double ac = Math.abs(origin.y() - destination.y());
        double cb = Math.abs(origin.x() - destination.x());

        double distance = Math.hypot(ac, cb);
        return calculate(distance, flightMode);
    }

    public long calculate(double distance, ShipNavFlightMode flightMode) {
        return transformers.get(flightMode).applyAsLong(distance);
    }
}
