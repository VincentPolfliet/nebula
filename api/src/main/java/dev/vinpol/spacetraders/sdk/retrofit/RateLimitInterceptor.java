package dev.vinpol.spacetraders.sdk.retrofit;

import dev.failsafe.RateLimiter;
import dev.vinpol.spacetraders.sdk.api.RateLimited;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import retrofit2.Invocation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

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
        Invocation invocation = req.tag(Invocation.class);

        Method method = Objects.requireNonNull(invocation).method();
        Class<?> clazz = method.getDeclaringClass();

        boolean methodIsRateLimited = clazz.isAnnotationPresent(RateLimited.class) || method.isAnnotationPresent(RateLimited.class);

        if (!methodIsRateLimited) {
            return chain.proceed(req);
        }

        if (smooth.tryAcquirePermit() || bursty.tryAcquirePermit()) {
            return chain.proceed(req);
        }

        throw new RuntimeException("Rate limit lock is required but couldn't be acquired for %s#%s".formatted(clazz.getSimpleName(), method.getName()));
    }
}
