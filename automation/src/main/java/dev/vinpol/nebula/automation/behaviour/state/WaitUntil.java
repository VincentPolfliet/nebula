package dev.vinpol.nebula.automation.behaviour.state;

import java.time.OffsetDateTime;

public record WaitUntil(OffsetDateTime waitUntil) implements ShipBehaviourResult {
}
