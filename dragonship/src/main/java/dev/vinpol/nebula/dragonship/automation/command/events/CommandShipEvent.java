package dev.vinpol.nebula.dragonship.automation.command.events;

import java.time.OffsetDateTime;

public record CommandShipEvent(String shipSymbol, OffsetDateTime now) {
}
