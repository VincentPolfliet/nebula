package dev.vinpol.nebula.dragonship.sdk;

import dev.vinpol.nebula.automation.ScheduledExecutor;
import dev.vinpol.nebula.automation.ShipBehaviourScheduler;
import dev.vinpol.nebula.automation.ShipCommander;
import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.nebula.automation.algorithms.ShipAlgorithm;
import dev.vinpol.nebula.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.automation.algorithms.excavator.ExcavatorAlgorithm;
import dev.vinpol.nebula.automation.behaviour.BehaviourFactoryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AutomationConfig {

    @Bean
    ShipEventNotifier shipEventNotifier() {
        return new ShipEventNotifier() {

        };
    }

    @Bean
    ExcavatorAlgorithm excavatorCommand(BehaviourFactoryRegistry automationFactory) {
        return new ExcavatorAlgorithm(automationFactory);
    }

    @Bean
    ScheduledExecutor shipTimer(TaskScheduler taskScheduler) {
        return (runnable, timestamp) -> taskScheduler.schedule(runnable, timestamp.toInstant());
    }

    @Bean
    ShipCommander shipCommander(List<ShipAlgorithm> algorithms, ShipBehaviourScheduler scheduler) {
        return new ShipCommander(
            new ShipAlgorithmResolver(
                algorithms.stream()
                    .collect(Collectors.toMap(ShipAlgorithm::forRole, Function.identity()))
            ),
            scheduler
        );
    }

    @Bean
    public ShipBehaviourScheduler scheduler(ScheduledExecutor scheduledExecutor) {
        return new ShipBehaviourScheduler(
            scheduledExecutor,
            Executors.newVirtualThreadPerTaskExecutor()
        );
    }
}
