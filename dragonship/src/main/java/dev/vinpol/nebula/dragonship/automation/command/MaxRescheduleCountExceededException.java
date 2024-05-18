package dev.vinpol.nebula.dragonship.automation.command;

import lombok.Getter;

@Getter
public class MaxRescheduleCountExceededException extends RuntimeException {

    private final String shipSymbol;
    private final int maxRescheduleCount;

    public MaxRescheduleCountExceededException(String message, String shipSymbol, int maxRescheduleCount) {
        super(message);
        this.shipSymbol = shipSymbol;
        this.maxRescheduleCount = maxRescheduleCount;
    }

}
