package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface ScheduledExecutor extends AutoCloseable {
    /**
     * Schedule a task to be executed at a specified timestamp.
     *
     * @param runnable  the task to be executed
     * @param timestamp the timestamp when the task should be executed
     *                  <p>
     *                  Note: The provided runnable should ideally be non-blocking to prevent delays in execution.
     */
    void scheduleAt(Runnable runnable, OffsetDateTime timestamp);

    default CompletableFuture<Void> scheduleAtAsCompletableFuture(Runnable runnable, OffsetDateTime timestamp) {
        return CompletableFuture.runAsync(runnable, command -> ScheduledExecutor.this.scheduleAt(command, timestamp));
    }

    static ScheduledExecutor ofScheduledExecutorService(ScheduledExecutorService executor) {
        return new ScheduledExecutorAdapter(executor);
    }

    @Override
    default void close() throws Exception {

    }

    class ScheduledExecutorAdapter implements ScheduledExecutor {
        private final ScheduledExecutorService executor;

        private ScheduledExecutorAdapter(ScheduledExecutorService executor) {
            this.executor = executor;
        }

        @Override
        public void scheduleAt(Runnable runnable, OffsetDateTime timestamp) {
            executor.schedule(runnable, calculateWaitUntil(timestamp).toSeconds(), TimeUnit.SECONDS);
        }

        private static Duration calculateWaitUntil(OffsetDateTime timestamp) {
            return Duration.between(OffsetDateTime.now(timestamp.getOffset()), timestamp);
        }

        @Override
        public void close() throws Exception {
            executor.close();
        }
    }
}
