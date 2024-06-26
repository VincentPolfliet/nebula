package dev.vinpol.nebula.dragonship.sdk;


import dev.failsafe.RateLimiter;
import dev.vinpol.nebula.dragonship.bigbrain.ThiccTank;
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
import org.dizitart.no2.Nitrite;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

import java.time.Clock;
import java.time.Duration;
import java.util.List;

@Configuration
@EnableConfigurationProperties({NebulaProperties.class})
public class ApiConfig {

    // https://github.com/SpaceTradersAPI/api-docs/wiki/Ratelimit
    private static final RateLimiter<Object> smoothRateLimiter = RateLimiter.smoothBuilder(2, Duration.ofSeconds(1)).withMaxWaitTime(Duration.ofSeconds(1)).build();
    private static final RateLimiter<Object> burstyRateLimiter = RateLimiter.burstyBuilder(10, Duration.ofSeconds(10)).withMaxWaitTime(Duration.ofSeconds(10)).build();

    @Bean
    public ApiClient apiClient(NebulaProperties nebulaProperties, List<Interceptor> interceptors, Nitrite n, Clock clock, TaskScheduler scheduler) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // api currently needs an authorization token until auto registration is created
        builder.addInterceptor(new HttpBearerAuth("Bearer", nebulaProperties.token()));

        // api is always rate limited
        builder.addInterceptor(new RateLimitInterceptor(smoothRateLimiter, burstyRateLimiter));

        // follow order used by spring @Order
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

        var realClient = new RetrofitApiClient(builder.build(), nebulaProperties.url());
        return new ThiccTank(realClient, n, clock, scheduler);
    }

    @Bean
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
