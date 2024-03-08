package dev.vinpol.nebula.dragonship.shared.time;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TimeConfig {

    @Bean
    Clock providesClock() {
        return Clock.systemUTC();
    }
}
