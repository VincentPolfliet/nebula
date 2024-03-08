package dev.vinpol.spacetraders.sdk.retrofit;

import dev.failsafe.RateLimiter;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RateLimitInterceptor implements Interceptor {

    private final RateLimiter<Object> rateLimiter;

    public RateLimitInterceptor(RateLimiter<Object> rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            rateLimiter.acquirePermit();
            return chain.proceed(chain.request());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
