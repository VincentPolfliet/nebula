package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.automation.ScheduledExecutor;
import dev.vinpol.nebula.automation.ShipBehaviourScheduler;
import dev.vinpol.nebula.automation.ShipCommander;
import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.nebula.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.automation.behaviour.DefaultShipBehaviourFactoryCreator;
import dev.vinpol.spacetraders.sdk.ApiClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class AutomationConfig {

    @Bean
    @ConditionalOnMissingBean
    Clock providesClock() {
        return Clock.systemUTC();
    }

    @Bean
    ShipCommander providesShipCommander(ShipAlgorithmResolver algorithmResolver, ShipBehaviourScheduler scheduler) {
        return new ShipCommander(algorithmResolver, scheduler);
    }

    @Bean
    @ConditionalOnMissingBean
    ShipBehaviourFactoryCreator behaviourFactoryRegistry(ApiClient apiClient, ShipEventNotifier shipEventNotifier) {
        return new DefaultShipBehaviourFactoryCreator(apiClient, shipEventNotifier);
    }

    @Bean
    ShipBehaviourScheduler providesBehaviourScheduler(ScheduledExecutor scheduledExecutor) {
        return new ShipBehaviourScheduler(scheduledExecutor, Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean
    @ConditionalOnMissingBean
    ShipEventNotifier providesNotifier(ApplicationEventPublisher eventPublisher, Clock clock) {
        return new ShipEventNotifierImpl(eventPublisher, clock);
    }

    // TODO: check if this works with @EnableScheduling
    @Bean
    @ConditionalOnMissingBean
    ScheduledExecutor providesScheduledTaskExecutor(TaskScheduler taskScheduler) {
        return new ScheduledTaskExecutor(taskScheduler);
    }
}
