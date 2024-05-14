package dev.vinpol.nebula.dragonship.shared.synchronize;

public interface SynchronizeTask<T> extends Runnable {
    String name();

    Class<T> typeClazz();
}
