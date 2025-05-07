package dev.vinpol.nebula.dragonship.automation.behaviour.state;

import java.time.OffsetDateTime;
import java.util.Objects;

public sealed interface ShipBehaviorResult permits Failed, Done, Success, WaitUntil {
    static ShipBehaviorResult success() {
        return new Success();
    }

    static ShipBehaviorResult done() {
        return new Done();
    }

    static ShipBehaviorResult waitUntil(OffsetDateTime waitUntil) {
        return new WaitUntil(waitUntil);
    }

    static ShipBehaviorResult failure(Exception e) {
        return new Failure(e.getMessage());
    }

    static ShipBehaviorResult failure(String message) {
        return new Failure(message);
    }

    static ShipBehaviorResult failure(FailureReason reason) {
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
        if (this instanceof FailureWithReason(FailureReason other)) {
            return Objects.equals(other, reason);
        }

        return false;
    }

    default boolean isWaitUntil() {
        return this instanceof WaitUntil;
    }

}
