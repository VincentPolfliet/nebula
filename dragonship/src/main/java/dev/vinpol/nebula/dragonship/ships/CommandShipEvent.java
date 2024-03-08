package dev.vinpol.nebula.dragonship.ships;

import java.time.OffsetDateTime;

public record CommandShipEvent(String shipSymbol, OffsetDateTime now) {
}
