package dev.vinpol.nebula.dragonship.automation.algorithms;


import dev.vinpol.nebula.dragonship.automation.algorithms.excavator.ExcavatorAlgorithm;
import dev.vinpol.nebula.dragonship.automation.behaviour.AutomationFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AlgorithmConfig {
    @Bean
    @ConditionalOnMissingBean
    ShipAlgorithmResolver shipAlgorithmResolver(List<ShipAlgorithm> shipAlgorithms) {
        return new ShipAlgorithmResolver(shipAlgorithms.stream()
            .collect(Collectors.toMap(ShipAlgorithm::forRole, Function.identity())));
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcavatorAlgorithm excavatorAlgorithm(AutomationFactory registry) {
        return new ExcavatorAlgorithm(registry);
    }
}
