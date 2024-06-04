package dev.vinpol.nebula.dragonship.automation.events.out;

import java.time.OffsetDateTime;

public record NavigatingToEvent(String shipSymbol, dev.vinpol.nebula.dragonship.sdk.WaypointSymbol destination,
                                OffsetDateTime arrival) {
}
