package dev.vinpol.nebula.dragonship.automation.events.out;

import java.time.OffsetDateTime;

public record WaitUntilCooldownEvent(String shipSymbol, OffsetDateTime cooldownExpired) {
}
