package dev.vinpol.nebula.dragonship.automation.algorithms;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ShipAlgorithmDescription {
    private final List<AvailableBehaviour> reasons = new ArrayList<>();

    public ShipAlgorithmDescription withFactory(String reason, ShipBehaviourFactory factory) {
        reasons.add(new AvailableBehaviour(reason, factory.parameters()));
        return this;
    }

    public ShipAlgorithmDescription withBehaviour(String behaviour, Map<String, String> parameters) {
        reasons.add(new AvailableBehaviour(behaviour, parameters));
        return this;
    }

    public List<AvailableBehaviour> build() {
        return reasons;
    }

    public ShipAlgorithmDescription withBehaviour(Behaviour behaviour, Map<String, String> params) {
        return withBehaviour(behaviour.name(), params);
    }
}
