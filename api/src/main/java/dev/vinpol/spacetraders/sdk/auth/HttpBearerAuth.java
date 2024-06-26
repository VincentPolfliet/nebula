package dev.vinpol.spacetraders.sdk.auth;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;

public final class HttpBearerAuth implements Interceptor {
    private static final String BEARER = "Bearer";

    private final String scheme;
    private final String bearerToken;

    public HttpBearerAuth(@Nullable String scheme, String bearerToken) {
        this.scheme = scheme;
        this.bearerToken = bearerToken;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        request = request.newBuilder()
            .addHeader("Authorization", (scheme != null ? upperCaseBearer(scheme) + " " : "") + bearerToken)
            .build();

        return chain.proceed(request);
    }

    private static String upperCaseBearer(String scheme) {
        return BEARER.equalsIgnoreCase(scheme) ? BEARER : scheme;
    }
}
