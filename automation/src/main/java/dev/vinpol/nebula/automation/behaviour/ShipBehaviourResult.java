package dev.vinpol.nebula.automation.behaviour;

import java.time.OffsetDateTime;

public sealed interface ShipBehaviourResult permits ShipBehaviourResult.Failed, ShipBehaviourResult.Done, ShipBehaviourResult.Success, ShipBehaviourResult.WaitUntil {
    static ShipBehaviourResult success() {
        return new Success();
    }

    static ShipBehaviourResult done() {
        return new Done();
    }

    static ShipBehaviourResult waitUntil(OffsetDateTime expiration) {
        return new WaitUntil(expiration);
    }

    static ShipBehaviourResult failure() {
        return new Failed();
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

    default boolean isWaitUntil() {
        return this instanceof WaitUntil;
    }

    record Success() implements ShipBehaviourResult {

    }

    record Failed() implements ShipBehaviourResult {

    }

    record Done() implements ShipBehaviourResult {

    }

    record WaitUntil(OffsetDateTime waitUntil) implements ShipBehaviourResult {
    }
}
