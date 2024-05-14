package dev.vinpol.nebula.dragonship.automation.algorithms;

import dev.vinpol.spacetraders.sdk.models.ShipRole;

import java.util.Map;
import java.util.Objects;

public class ShipAlgorithmResolver {
    private final Map<ShipRole, ShipAlgorithm> algorithms;

    public ShipAlgorithmResolver(Map<ShipRole, ShipAlgorithm> algorithms) {
        this.algorithms = Objects.requireNonNull(algorithms);
    }

    public ShipAlgorithm resolve(ShipRole role) {
        Objects.requireNonNull(role);

        return algorithms.getOrDefault(role, null);
    }
}
