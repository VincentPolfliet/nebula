package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.dragonship.automation.ScheduledTaskExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.TaskScheduler;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ScheduledTaskExecutorTest {

    @Test
    void scheduleAt() {
        TaskScheduler taskScheduler = mock(TaskScheduler.class);

        Runnable runnable = () -> {
        };
        OffsetDateTime timestamp = OffsetDateTime.now();

        ScheduledTaskExecutor sut = new ScheduledTaskExecutor(taskScheduler);

        sut.scheduleAt(runnable, timestamp);

        verify(taskScheduler).schedule(runnable, timestamp.toInstant());
    }
}
