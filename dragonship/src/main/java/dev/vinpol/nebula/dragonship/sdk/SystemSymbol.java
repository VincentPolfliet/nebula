package dev.vinpol.nebula.dragonship.sdk;

import java.util.Objects;

public record SystemSymbol(String sector, String system) {

    public static SystemSymbol tryParse(String input) {
        Objects.requireNonNull(input, "input");

        String[] splittedInput = input.split("-");

        if (splittedInput.length != 2) {
            throw new IllegalStateException("The input '%s' is not splittable in two parts using '-'".formatted(input));
        }

        return new SystemSymbol(
                splittedInput[0],
                input
        );
    }
}
