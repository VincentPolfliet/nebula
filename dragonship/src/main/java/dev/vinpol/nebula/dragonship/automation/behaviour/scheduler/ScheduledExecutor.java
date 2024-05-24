package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface ScheduledExecutor {
    /**
     * Schedule a task to be executed at a specified timestamp.
     *
     * @param runnable  the task to be executed
     * @param timestamp the timestamp when the task should be executed
     *                  <p>
     *                  Note: The provided runnable should ideally be non-blocking to prevent delays in execution.
     */
    CompletableFuture<Void> scheduleAt(Runnable runnable, OffsetDateTime timestamp);

    <T> CompletableFuture<T> scheduleAt(Supplier<T> supplier, OffsetDateTime timestamp);

    static ScheduledExecutor ofExecutor(Executor executor) {
        return new ExecutorAdapter(executor);
    }

    class ExecutorAdapter implements ScheduledExecutor {
        private final Executor executor;

        private ExecutorAdapter(Executor executor) {
            this.executor = executor;
        }

        public CompletableFuture<Void> scheduleAt(Runnable runnable, OffsetDateTime timestamp) {
            return CompletableFuture.runAsync(runnable, delayedExecutor(timestamp));
        }

        public <T> CompletableFuture<T> scheduleAt(Supplier<T> supplier, OffsetDateTime timestamp) {
            return CompletableFuture.supplyAsync(supplier, delayedExecutor(timestamp));
        }

        private @NotNull Executor delayedExecutor(OffsetDateTime timestamp) {
            return CompletableFuture.delayedExecutor(calculateWaitUntil(timestamp).toSeconds(), TimeUnit.SECONDS, executor);
        }

        private static Duration calculateWaitUntil(OffsetDateTime timestamp) {
            return Duration.between(OffsetDateTime.now(timestamp.getOffset()), timestamp);
        }
    }
}
