package dev.vinpol.nebula.dragonship.sdk;


import dev.failsafe.RateLimiter;
import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.RetrofitApiClient;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.auth.HttpBearerAuth;
import dev.vinpol.spacetraders.sdk.retrofit.RateLimitInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableConfigurationProperties({NebulaProperties.class})
public class ApiConfig {

    @Bean
    @ConditionalOnMissingBean
    public ApiClient apiClient(NebulaProperties nebulaProperties, List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // api currently needs an authorization token until auto registration is created
        builder.addInterceptor(new HttpBearerAuth("Bearer", nebulaProperties.token()));

        // follow order used by spring @Order
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

        // api is always rate limited
        builder.addInterceptor(new RateLimitInterceptor(RateLimiter.smoothBuilder(2, Duration.ofSeconds(1)).build()));

        return new RetrofitApiClient(builder.build(), nebulaProperties.url());
    }

    @Bean
    @ConditionalOnClass(HttpLoggingInterceptor.class)
    Interceptor providesLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.redactHeader("Authorization");
        logging.level(HttpLoggingInterceptor.Level.BASIC);

        return logging;
    }

    @Bean
    AgentsApi providesAgentsApi(ApiClient client) {
        return client.agentsApi();
    }

    @Bean
    SystemsApi providesSystemsApi(ApiClient client) {
        return client.systemsApi();
    }

    @Bean
    ContractsApi providesContractsApi(ApiClient client) {
        return client.contractsApi();
    }

    @Bean
    FleetApi providesFleetApi(ApiClient apiClient) {
        return apiClient.fleetApi();
    }
}
