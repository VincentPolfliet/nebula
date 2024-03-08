package dev.vinpol.nebula.dragonship.shared.time;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Component
public record TimeWizard(Clock clock) {
    public OffsetDateTime now() {
        return OffsetDateTime.now(clock);
    }

    public LocalDate today() {
        return now().toLocalDate();
    }
}
