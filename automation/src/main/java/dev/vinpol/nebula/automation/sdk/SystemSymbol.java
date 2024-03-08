package dev.vinpol.nebula.automation.sdk;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public record SystemSymbol(String sector, String system) {

    public static SystemSymbol tryParse(String input) {
        Objects.requireNonNull(input, "input");

        String[] splittedInput = input.split("-");

        Validate.isTrue(splittedInput.length == 2, "The input '%s' is not splittable in two parts using '-'".formatted(input));

        return new SystemSymbol(
                splittedInput[0],
                input
        );
    }
}
