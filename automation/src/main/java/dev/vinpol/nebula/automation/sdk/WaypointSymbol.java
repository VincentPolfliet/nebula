package dev.vinpol.nebula.automation.sdk;

import java.util.Objects;

public record WaypointSymbol(String sector, String system, String waypoint) {

    public SystemSymbol toSystemSymbol() {
        return new SystemSymbol(sector, system);
    }

    public static WaypointSymbol tryParse(String input) {
        Objects.requireNonNull(input, "input");

        String[] splittedInput = input.split("-");
        if (splittedInput.length != 3) {
            throw new IllegalStateException( "The input '%s' is not splittable in three parts using '-'".formatted(input));
        }

        return new WaypointSymbol(
                splittedInput[0],
                splittedInput[0] + "-" + splittedInput[1],
                input
        );
    }
}
