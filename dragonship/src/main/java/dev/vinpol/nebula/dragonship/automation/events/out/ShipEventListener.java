package dev.vinpol.nebula.dragonship.automation.events.out;

public interface ShipEventListener {
    default void onCargoIsFull(CargoIsFullEvent event) {

    }

    default void onFuelIsAlmostEmpty(FuelIsAlmostEmptyEvent event) {

    }

    default void onWaitUntilArrival(NavigatingToEvent event) {

    }

    default void onWaitCooldown(WaitUntilCooldownEvent event) {

    }
}
