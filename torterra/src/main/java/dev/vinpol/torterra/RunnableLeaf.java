package dev.vinpol.torterra;

import java.util.Objects;

final class RunnableLeaf extends Leaf<Void> implements Runnable {

    private final Runnable runnable;

    public RunnableLeaf(Runnable runnable) {
        super();
        this.runnable = Objects.requireNonNull(runnable, "runnable");
    }

    @Override
    public void act(Void instance) {
        run();
    }

    @Override
    public void run() {
        try {
            runnable.run();
            succeed();
        } catch (RuntimeException e) {
            fail();
        }
    }
}
