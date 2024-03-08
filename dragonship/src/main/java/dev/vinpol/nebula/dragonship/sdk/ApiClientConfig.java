package dev.vinpol.nebula.dragonship.sdk;

import dev.failsafe.RateLimiter;
import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.RetrofitApiClient;
import dev.vinpol.spacetraders.sdk.auth.HttpBearerAuth;
import dev.vinpol.spacetraders.sdk.retrofit.RateLimitInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ApiClientConfig {

    @Bean
    public ApiClient apiClient(@Value("${spacetraders.token}") String token, @Value("${spacetraders.base-url}") String url) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.redactHeader("Authorization");
        logging.level(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpBearerAuth("Bearer", token))
                .addInterceptor(logging)
                .addInterceptor(new RateLimitInterceptor(RateLimiter.smoothBuilder(2, Duration.ofSeconds(1)).build()))
                .build();

        return new RetrofitApiClient(client, url);
    }
}
