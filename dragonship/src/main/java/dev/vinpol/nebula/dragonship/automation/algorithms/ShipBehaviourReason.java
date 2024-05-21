package dev.vinpol.nebula.dragonship.automation.algorithms;

import java.util.Map;

public record ShipBehaviourReason(BehaviourReason reason, Map<String, String> parameters) {
}
