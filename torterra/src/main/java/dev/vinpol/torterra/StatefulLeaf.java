package dev.vinpol.torterra;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class StatefulLeaf<T> implements Leaf<T> {

    @Getter(AccessLevel.PROTECTED)
    private LeafState state = LeafState.running();

    @Override
    public final LeafState act(T instance) {
        if (!isRunning()) {
            return state;
        }

        doAct(instance);
        return state;
    }

    protected abstract void doAct(T instance);

    protected void succeed() {
        state = LeafState.success();
    }

    protected void fail() {
        state = LeafState.failed();
    }

    public boolean isFailure() {
        return state instanceof FailedState;
    }

    public boolean isSuccess() {
        return state instanceof SuccessState;
    }

    public boolean isRunning() {
        return state instanceof RunningState;
    }
}
