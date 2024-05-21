package dev.vinpol.nebula.dragonship.automation.algorithms;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ShipAlgorithmDescription {
    private final List<ShipBehaviourReason> reasons = new ArrayList<>();

    public ShipAlgorithmDescription withFactory(BehaviourReason reason, ShipBehaviourFactory factory) {
        reasons.add(new ShipBehaviourReason(reason, factory.parameters()));
        return this;
    }

    public ShipAlgorithmDescription withReason(BehaviourReason reason, Map<String, String> parameters) {
        reasons.add(new ShipBehaviourReason(reason, parameters));
        return this;
    }

    public List<ShipBehaviourReason> build() {
        return reasons;
    }
}
