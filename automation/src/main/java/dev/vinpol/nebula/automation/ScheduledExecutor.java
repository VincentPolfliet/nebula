package dev.vinpol.nebula.automation;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface ScheduledExecutor {
    /**
     * Schedule a task to be executed at a specified timestamp.
     *
     * @param runnable  the task to be executed
     * @param timestamp the timestamp when the task should be executed
     *                  <p>
     *                  Note: The provided runnable should ideally be non-blocking to prevent delays in execution.
     */
    void scheduleAt(Runnable runnable, OffsetDateTime timestamp);

    static ScheduledExecutor ofScheduledExecutorService(ScheduledExecutorService executor) {
        return new ScheduledExecutorAdapter(executor);
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
    }
}
