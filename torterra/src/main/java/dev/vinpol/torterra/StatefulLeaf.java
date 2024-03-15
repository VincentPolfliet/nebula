package dev.vinpol.torterra;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class StatefulLeaf<T> implements Leaf<T> {

    @Getter(AccessLevel.PROTECTED)
    private LeafState state = LeafState.RUNNING;

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
        state = LeafState.SUCCESS;
    }

    protected void fail() {
        state = LeafState.FAILED;
    }

    public boolean isFailure() {
        return state == LeafState.FAILED;
    }

    public boolean isSuccess() {
        return state == LeafState.SUCCESS;
    }

    public boolean isRunning() {
        return state == LeafState.RUNNING;
    }
}
