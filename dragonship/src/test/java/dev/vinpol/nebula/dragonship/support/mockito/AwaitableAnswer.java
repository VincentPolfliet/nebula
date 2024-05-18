package dev.vinpol.nebula.dragonship.support.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class AwaitableAnswer<T> implements Answer<T> {

    private final Supplier<T> supplier;
    private final AtomicBoolean lock = new AtomicBoolean(true);

    public AwaitableAnswer(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> AwaitableAnswer<T> await(Runnable runnable) {
        return new AwaitableAnswer<>(() -> {
            runnable.run();
            return null;
        });
    }

    public static <T> AwaitableAnswer<T> await(Supplier<T> supplier) {
        return new AwaitableAnswer<>(supplier);
    }

    public void release() {
        lock.set(false);
    }

    @Override
    public T answer(InvocationOnMock invocationOnMock) throws Throwable {
        while (lock.get()) {
            // wait until lock gets released
        }

        return supplier.get();
    }
}
