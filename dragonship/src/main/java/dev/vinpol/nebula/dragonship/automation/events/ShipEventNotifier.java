package dev.vinpol.nebula.dragonship.automation.events;

import java.time.OffsetDateTime;

public interface ShipEventNotifier {

    default void setWaitUntilArrival(String shipSymbol, OffsetDateTime arrival) {

    }

    default void setWaitUntilCooldown(String shipSymbol, OffsetDateTime expiration) {

    }

    default void setCargoFull(String shipSymbol) {

    }

    default void setFuelIsAlmostEmpty(String shipSymbol) {

    }
}
