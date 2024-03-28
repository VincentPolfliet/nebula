package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.automation.algorithms.ShipAlgorithm;
import dev.vinpol.nebula.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.automation.algorithms.excavator.ExcavatorAlgorithm;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviourFactoryCreator;
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
    public ExcavatorAlgorithm excavatorAlgorithm(ShipBehaviourFactoryCreator registry) {
        return new ExcavatorAlgorithm(registry);
    }
}
