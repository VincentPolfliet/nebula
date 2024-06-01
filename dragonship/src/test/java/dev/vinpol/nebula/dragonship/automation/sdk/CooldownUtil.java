package dev.vinpol.nebula.dragonship.automation.sdk;

import dev.vinpol.spacetraders.sdk.models.Cooldown;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class CooldownUtil {
    private CooldownUtil() {

    }

    public static Cooldown cooldown(OffsetDateTime until) {
        OffsetDateTime now = OffsetDateTime.now();

        if (until.isAfter(now)) {
            return new Cooldown()
                .remainingSeconds(0)
                .totalSeconds(0)
                .expiration(null);
        }

        return new Cooldown()
            .remainingSeconds((int) ChronoUnit.SECONDS.between(now, until))
            .totalSeconds(60)
            .expiration(until);
    }
}
