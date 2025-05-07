package dev.vinpol.spacetraders.sdk;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.vinpol.spacetraders.sdk.api.*;
import dev.vinpol.spacetraders.sdk.retrofit.SynchronousCallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitApiClient implements ApiClient {

    private final String url;
    private final Retrofit retrofit;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper json;


    public RetrofitApiClient() {
        this(new OkHttpClient(), "https://api.spacetraders.io/v2/");
    }

    public RetrofitApiClient(OkHttpClient client, String url) {
        this(client, getJsonFromClassPath(), url);
    }

    public RetrofitApiClient(OkHttpClient client, ObjectMapper json, String url) {
        this.okHttpClient = client;
        this.json = json;
        this.url = url;
        this.retrofit = createDefaultAdapter();
    }

    private Retrofit createDefaultAdapter() {
        return new Retrofit
            .Builder()
            .baseUrl(url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(json))
            .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
            .client(okHttpClient)
            .build();
    }

    private static ObjectMapper getJsonFromClassPath() {
        return new ObjectMapper()
            .findAndRegisterModules()
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    @Override
    public DefaultApi defaults() {
        return createService(DefaultApi.class);
    }

    @Override
    public AgentsApi agentsApi() {
        return createService(AgentsApi.class);
    }

    @Override
    public FleetApi fleetApi() {
        return createService(FleetApi.class);
    }

    @Override
    public SystemsApi systemsApi() {
        return createService(SystemsApi.class);
    }

    @Override
    public ContractsApi contractsApi() {
        return createService(ContractsApi.class);
    }
}

