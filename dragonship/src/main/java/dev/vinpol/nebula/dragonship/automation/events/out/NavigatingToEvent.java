package dev.vinpol.nebula.dragonship.automation.events.out;

import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;

import java.time.OffsetDateTime;

public record NavigatingToEvent(String shipSymbol, WaypointSymbol destination,
                                OffsetDateTime arrival) {
}
