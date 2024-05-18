package dev.vinpol.nebula.dragonship.utils.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public record TimeWizard(Clock clock) {
    public OffsetDateTime now() {
        return OffsetDateTime.now(clock);
    }

    public LocalDate today() {
        return now().toLocalDate();
    }

    public long differenceInSeconds(OffsetDateTime other) {
        return ChronoUnit.SECONDS.between(now(), other);
    }
}
