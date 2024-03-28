package dev.vinpol.nebula.dragonship.sdk;

import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ApiConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withPropertyValues("nebula.st.token=token", "nebula.st.url=https://api.spacetraders.io/v2/")
        .withConfiguration(AutoConfigurations.of(ApiConfig.class));


    @Test
    void testNoHttpLoggingInterceptor() {
        contextRunner
            .withClassLoader(new FilteredClassLoader(HttpLoggingInterceptor.class))
            .run(context -> assertThat(context).doesNotHaveBean(HttpLoggingInterceptor.class));
    }

    @Test
    void testHasApiClient() {
        contextRunner.run((context) -> {
            assertThat(context).hasSingleBean(ApiClient.class);
            assertThat(context)
                .hasSingleBean(AgentsApi.class)
                .hasSingleBean(SystemsApi.class)
                .hasSingleBean(ContractsApi.class)
                .hasSingleBean(FleetApi.class);
        });
    }
}
