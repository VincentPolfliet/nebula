package dev.vinpol.spacetraders.sdk.retrofit;

import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class SynchronousCallAdapterFactory extends CallAdapter.Factory {
    SynchronousCallAdapterFactory() {
    }

    public static SynchronousCallAdapterFactory create() {
        return new SynchronousCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(@NotNull Type returnType, @NotNull Annotation[] annotations, @NotNull Retrofit retrofit) {
        if (getRawType(returnType) == Call.class) {
            return null;
        }

        return new SynchronousCallAdapter<>(returnType);
    }

    private record SynchronousCallAdapter<T>(Type responseType) implements CallAdapter<T, T> {

        @NotNull
        @Override
        public T adapt(Call<T> call) {
            try {
                Response<T> response = call.execute();

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    return response.body();
                } else {
                    throw new RuntimeException("Response not successful: " + response.code());
                }
            } catch (IOException e) {
                Request request = call.request();
                throw new RuntimeException("error executing call to '%s'".formatted(request.url()), e);
            }
        }
    }
}
