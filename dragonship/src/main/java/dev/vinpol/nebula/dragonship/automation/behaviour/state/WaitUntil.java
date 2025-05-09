package dev.vinpol.nebula.dragonship.automation.behaviour.state;

import java.time.OffsetDateTime;

public record WaitUntil(OffsetDateTime timestamp) implements ShipBehaviorResult {
}
