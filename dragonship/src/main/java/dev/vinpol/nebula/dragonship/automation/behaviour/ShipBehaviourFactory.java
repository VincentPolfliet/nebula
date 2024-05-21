package dev.vinpol.nebula.dragonship.automation.behaviour;

import java.util.Collections;
import java.util.Map;

public interface ShipBehaviourFactory {
    default Map<String, String> parameters() {
        return Collections.emptyMap();
    }

    ShipBehaviour create();
}
