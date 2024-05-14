package dev.vinpol.nebula.dragonship.automation.behaviour.state;

import java.time.OffsetDateTime;
import java.util.Objects;

public sealed interface ShipBehaviourResult permits Failed, Done, Success, WaitUntil {
    static ShipBehaviourResult success() {
        return new Success();
    }

    static ShipBehaviourResult done() {
        return new Done();
    }

    static ShipBehaviourResult waitUntil(OffsetDateTime waitUntil) {
        return new WaitUntil(waitUntil);
    }

    static ShipBehaviourResult failure() {
        return new Failure();
    }

    static ShipBehaviourResult failure(String message) {
        Objects.requireNonNull(message);

        return new FailureWithDetail(message);
    }

    static ShipBehaviourResult failure(FailureReason reason) {
        Objects.requireNonNull(reason);

        return new FailureWithReason(reason);
    }

    default boolean isDone() {
        return this instanceof Done;
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return this instanceof Failed;
    }

    default boolean hasFailedWithReason(FailureReason reason) {
        if (this instanceof FailureWithReason r) {
            return Objects.equals(r.reason(), reason);
        }

        return false;
    }

    default boolean isWaitUntil() {
        return this instanceof WaitUntil;
    }

}
