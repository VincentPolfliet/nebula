package dev.vinpol.nebula.dragonship;

import dev.vinpol.nebula.dragonship.automation.AlgorithmConfig;
import dev.vinpol.nebula.dragonship.automation.AutomationConfig;
import dev.vinpol.nebula.dragonship.sdk.ApiConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {
    AutomationConfig.class,
    AlgorithmConfig.class,
    ApiConfig.class,
    TaskSchedulingAutoConfiguration.class
}, properties = {"nebula.st.token=token", "nebula.st.url=http://localhost:1337/api/"})
public class CucumberSpringConfiguration {

}
