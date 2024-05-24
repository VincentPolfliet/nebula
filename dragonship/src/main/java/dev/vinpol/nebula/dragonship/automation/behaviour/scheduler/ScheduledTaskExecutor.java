package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.TaskScheduler;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ScheduledTaskExecutor implements ScheduledExecutor {

    private final TaskScheduler taskScheduler;

    public ScheduledTaskExecutor(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public CompletableFuture<Void> scheduleAt(Runnable runnable, OffsetDateTime timestamp) {
        Objects.requireNonNull(runnable);
        Objects.requireNonNull(timestamp);

        return CompletableFuture.runAsync(runnable, inRunnable -> taskScheduler.schedule(inRunnable, timestamp.toInstant()));
    }

    @Override
    public <T> CompletableFuture<T> scheduleAt(Supplier<T> supplier, OffsetDateTime timestamp) {
        return CompletableFuture.supplyAsync(supplier, inRunnable -> taskScheduler.schedule(inRunnable, timestamp.toInstant()));
    }
}
