package dev.vinpol.nebula.dragonship.automation.events.out;

import java.io.IOException;

public interface ShipEventListener {
    default void onCargoIsFull(CargoIsFullEvent event) throws IOException {

    }

    default void onFuelIsAlmostEmpty(FuelIsAlmostEmptyEvent event) throws IOException {

    }

    default void onWaitUntilArrival(NavigatingToEvent event) throws IOException {

    }

    void onArrivedAt(ArrivatedAtDestinationEvent event) throws IOException;

    default void onWaitCooldown(WaitUntilCooldownEvent event) throws IOException {

    }
}
