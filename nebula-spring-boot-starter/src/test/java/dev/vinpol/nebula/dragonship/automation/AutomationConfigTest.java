package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.dragonship.sdk.ApiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.TaskScheduler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {
    AutomationConfig.class,
    AlgorithmConfig.class,
    ApiConfig.class,
    TaskSchedulingAutoConfiguration.class
}, properties = {"nebula.st.token=token", "nebula.st.url=https://api.spacetraders.io/v2/"})
class AutomationConfigTest {

    // TaskScheduler is enabled using AutomationConfig
    @Autowired
    TaskScheduler scheduler;

    @Test
    void hasTaskScheduler() {
        // this tests the @EnableScheduling annotation on AutomationConfig
        assertThat(scheduler).isNotNull();
    }
}
