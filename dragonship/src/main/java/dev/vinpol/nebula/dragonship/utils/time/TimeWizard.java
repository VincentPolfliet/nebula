package dev.vinpol.nebula.dragonship.utils.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record TimeWizard(Clock clock) {
    public OffsetDateTime now() {
        return OffsetDateTime.now(clock);
    }

    public LocalDate today() {
        return now().toLocalDate();
    }
}
