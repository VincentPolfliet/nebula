package dev.vinpol.nebula.dragonship.automation.events.out;

import java.time.OffsetDateTime;

public record WaitUntilArrivalEvent(String shipSymbol, OffsetDateTime arrival) {
}
