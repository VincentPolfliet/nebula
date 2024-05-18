package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ShipBehaviourScheduler;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ScheduledExecutor;
import dev.vinpol.nebula.dragonship.automation.behaviour.scheduler.ScheduledTaskExecutor;
import dev.vinpol.nebula.dragonship.automation.command.ShipCommander;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifierImpl;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
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
    ShipCommander providesShipCommander(ShipBehaviourScheduler scheduler) {
        return new ShipCommander(scheduler);
    }

    @Bean
    ShipBehaviourScheduler providesBehaviourScheduler(FleetApi fleetApi, ScheduledExecutor scheduledExecutor) {
        return new ShipBehaviourScheduler(fleetApi, scheduledExecutor, Executors.newVirtualThreadPerTaskExecutor());
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
