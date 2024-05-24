package dev.vinpol.nebula.dragonship.support.awaitable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class AwaitableObject<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private final AtomicBoolean lock = new AtomicBoolean(true);

    public AwaitableObject(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> AwaitableObject<T> await(Runnable runnable) {
        return new AwaitableObject<>(() -> {
            runnable.run();
            return null;
        });
    }

    public static <T> AwaitableObject<T> await(Supplier<T> supplier) {
        return new AwaitableObject<>(supplier);
    }

    public void release() {
        lock.set(false);
    }

    @Override
    public T get() {
        while (lock.get()) {
            // wait until lock gets released
        }

        return supplier.get();
    }
}
