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

    private final RateLimiter<Object> rateLimiter;

    public RateLimitInterceptor(RateLimiter<Object> rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request req = chain.request();

            Invocation invocation = req.tag(Invocation.class);

            if (invocation == null) {
                return chain.proceed(req);
            }

            Method method = invocation.method();
            Class<?> clazz = method.getDeclaringClass();

            boolean isRateLimited = clazz.isAnnotationPresent(RateLimited.class) || method.isAnnotationPresent(RateLimited.class);

            if (isRateLimited) {
                rateLimiter.acquirePermit();
            }

            return chain.proceed(req);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
