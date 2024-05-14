package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import org.springframework.scheduling.TaskScheduler;

import java.time.OffsetDateTime;
import java.util.Objects;

public class ScheduledTaskExecutor implements ScheduledExecutor {

    private final TaskScheduler taskScheduler;

    public ScheduledTaskExecutor(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void scheduleAt(Runnable runnable, OffsetDateTime timestamp) {
        Objects.requireNonNull(runnable);
        Objects.requireNonNull(timestamp);

        taskScheduler.schedule(runnable, timestamp.toInstant());
    }
}
