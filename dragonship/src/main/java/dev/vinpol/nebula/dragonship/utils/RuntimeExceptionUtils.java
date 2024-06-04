package dev.vinpol.nebula.dragonship.utils;

public class RuntimeExceptionUtils {
    public static void rethrowIfPossible(Throwable throwable) {
        if (throwable instanceof RuntimeException e) {
            throw e;
        }

        throw new RuntimeException(throwable);
    }
}
