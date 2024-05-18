package dev.vinpol.torterra;

public sealed interface LeafState permits FailedState, RunningState, SuccessState {

    static LeafState success() {
        return new SuccessState();
    }

    static LeafState running() {
        return new RunningState();
    }

    static LeafState failed() {
        return new FailedState();
    }

    default boolean isFailure() {
        return this instanceof FailedState;
    }

    default boolean isSuccess() {
        return this instanceof SuccessState;
    }

    default boolean isRunning() {
        return this instanceof RunningState;
    }
}
