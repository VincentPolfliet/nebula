package dev.vinpol.nebula.dragonship.sdk;


import dev.vinpol.spacetraders.sdk.RetrofitApiClient;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    AgentsApi providesAgentsApi(RetrofitApiClient client) {
        return client.createService(AgentsApi.class);
    }

    @Bean
    SystemsApi providesSystemsApi(RetrofitApiClient client) {
        return client.createService(SystemsApi.class);
    }

    @Bean
    ContractsApi providesContractsApi(RetrofitApiClient client) {
        return client.createService(ContractsApi.class);
    }

    @Bean
    FleetApi providesFleetApi(RetrofitApiClient apiClient) {
        return apiClient.createService(FleetApi.class);
    }
}
