package dev.vinpol.nebula.automation;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;

public class ThreadUtils {

    public static void waitUntil(OffsetDateTime timestamp) {
        Objects.requireNonNull(timestamp);

        try {
            Duration duration = calculateWaitUntil(timestamp);
            // no op if duration is negative
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Duration calculateWaitUntil(OffsetDateTime timestamp) {
        return Duration.between(OffsetDateTime.now(timestamp.getOffset()), timestamp);
    }
}
