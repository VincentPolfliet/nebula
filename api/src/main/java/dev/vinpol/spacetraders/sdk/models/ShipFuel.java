package dev.vinpol.spacetraders.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * Details of the ship&#39;s fuel tanks including how much fuel was consumed during the last transit or action.
 */
@Data
public class ShipFuel {
    private int current;
    private int capacity;
    private ShipFuelConsumed consumed;

    public static ShipFuel infinite() {
        return new ShipFuel();
    }

    public ShipFuel current(int current) {
        this.current = current;
        return this;
    }

    public ShipFuel capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public ShipFuel consumed(ShipFuelConsumed consumed) {
        this.consumed = consumed;
        return this;
    }

    @JsonIgnore
    public boolean isNotInfinite() {
        // fuel should be considered infinite if the capacity and current volume are both 0
        return current != 0 || capacity != 0;
    }

    @JsonIgnore
    public boolean isFull() {
        return current == capacity;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return current == 0;
    }

    public boolean shouldConsiderEmpty(double maxPercentage) {
        return shouldConsiderEmpty(0, maxPercentage);
    }

    public boolean shouldConsiderEmpty(double minPercentage, double maxPercentage) {
        double currentPercentage = (double) current / capacity;
        return currentPercentage >= minPercentage && currentPercentage <= maxPercentage;
    }
}

