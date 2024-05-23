package dev.vinpol.nebula.dragonship.support.mockito;

import dev.vinpol.nebula.dragonship.support.awaitable.AwaitableObject;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.function.Supplier;

public class AwaitableAnswer<T> implements Answer<T> {

    private final AwaitableObject<T> awaitable;

    private AwaitableAnswer(AwaitableObject<T> awaitable) {
        this.awaitable = awaitable;
    }

    public static <T> AwaitableAnswer<T> await(Runnable runnable) {
        return new AwaitableAnswer<>(new AwaitableObject<>(
            () -> {
                runnable.run();
                return null;
            }
        ));
    }

    public static <T> AwaitableAnswer<T> await(Supplier<T> supplier) {
        return new AwaitableAnswer<>(new AwaitableObject<>(supplier));
    }

    public void release() {
        awaitable.release();
    }

    @Override
    public T answer(InvocationOnMock invocationOnMock) throws Throwable {
        return awaitable.get();
    }
}
