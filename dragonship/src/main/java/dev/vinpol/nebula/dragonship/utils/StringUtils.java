package dev.vinpol.nebula.dragonship.utils;

public class StringUtils {

    private StringUtils() {

    }

    public static String appendIfNotStartsWith(char ch, String str) {
        if (str == null || str.isEmpty()) {
            return String.valueOf(ch);
        }

        if (str.charAt(0) != ch) {
            return ch + str;
        }

        return str;
    }
}
