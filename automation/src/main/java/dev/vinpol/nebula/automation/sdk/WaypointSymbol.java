package dev.vinpol.nebula.automation.sdk;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public record WaypointSymbol(String sector, String system, String waypoint) {

    public SystemSymbol toSystemSymbol() {
        return new SystemSymbol(sector, system);
    }

    public static WaypointSymbol tryParse(String input) {
        Objects.requireNonNull(input, "input");

        String[] splittedInput = input.split("-");
        Validate.isTrue(splittedInput.length == 3, "The input '%s' is not splittable in three parts using '-'".formatted(input));

        return new WaypointSymbol(
                splittedInput[0],
                splittedInput[0] + "-" + splittedInput[1],
                input
        );
    }
}
