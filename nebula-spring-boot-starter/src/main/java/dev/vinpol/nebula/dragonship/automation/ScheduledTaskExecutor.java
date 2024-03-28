package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.automation.ScheduledExecutor;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.TaskScheduler;

import java.time.OffsetDateTime;
import java.util.Objects;

@AllArgsConstructor
public class ScheduledTaskExecutor implements ScheduledExecutor {

    private final TaskScheduler taskScheduler;

    @Override
    public void scheduleAt(Runnable runnable, OffsetDateTime timestamp) {
        Objects.requireNonNull(runnable);
        Objects.requireNonNull(timestamp);

        taskScheduler.schedule(runnable, timestamp.toInstant());
    }
}
