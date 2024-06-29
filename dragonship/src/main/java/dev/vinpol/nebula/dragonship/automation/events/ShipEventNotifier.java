package dev.vinpol.nebula.dragonship.automation.events;

import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;

import java.time.OffsetDateTime;

public interface ShipEventNotifier {

    default void setNavigatingTo(String shipSymbol, WaypointSymbol destination, OffsetDateTime arrival) {

    }

    default void setOrbited(String symbol) {

    }

    default void setDocked(String symbol) {

    }

    default void setArrivedAt(String shipSymbol, WaypointSymbol destination, OffsetDateTime arrivedAt) {

    }

    default void setWaitUntilCooldown(String shipSymbol, OffsetDateTime expiration) {

    }

    default void setCargoFull(String shipSymbol) {

    }

    default void setFuelIsAlmostEmpty(String shipSymbol) {

    }
}
