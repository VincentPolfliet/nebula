package dev.vinpol.spacetraders.sdk.models;

import java.util.concurrent.ThreadLocalRandom;

public class ShipSymbolGenerator {
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789-";

    private ShipSymbolGenerator() {

    }

    public static String generate() {
        StringBuilder callsign = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            int index = ThreadLocalRandom.current().nextInt(ALLOWED_CHARACTERS.length());
            callsign.append(ALLOWED_CHARACTERS.charAt(index));
        }
        return callsign.toString();
    }

    public static String generateFromString(String input) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Make input all uppercase
        input = input.toUpperCase();

        // Shorten input if longer than 14 characters
        if (input.length() > 14) {
            input = input.substring(0, 14);
        }

        // Replace letters with random characters and vice versa
        StringBuilder callsign = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                // Replace letter with random character or keep it
                if (random.nextBoolean()) {
                    int index = random.nextInt(ALLOWED_CHARACTERS.length());
                    callsign.append(ALLOWED_CHARACTERS.charAt(index));
                } else {
                    callsign.append(c);
                }
            } else if (ALLOWED_CHARACTERS.contains(String.valueOf(c))) {
                // Keep digit or allowed special character
                callsign.append(c);
            }
        }

        return callsign.toString();
    }
}
