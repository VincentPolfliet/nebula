package dev.vinpol.nebula.dragonship.automation.events;

import java.time.OffsetDateTime;

public record WaitUntilCooldownEvent(String shipSymbol, OffsetDateTime cooldownExpired) {
}
