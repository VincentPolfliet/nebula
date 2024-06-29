package dev.vinpol.spacetraders.sdk.retrofit;

import dev.failsafe.Failsafe;
import dev.failsafe.RateLimiter;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RateLimitInterceptor implements Interceptor {

    private final RateLimiter<Object> smooth;
    private final RateLimiter<Object> bursty;

    public RateLimitInterceptor(RateLimiter<Object> smooth, RateLimiter<Object> bursty) {
        assert smooth.isSmooth();
        assert bursty.isBursty();

        this.smooth = smooth;
        this.bursty = bursty;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();

        return Failsafe.with(
            smooth,
            bursty
        ).get(() -> chain.proceed(chain.request()));
    }
}
