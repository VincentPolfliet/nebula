package dev.vinpol.nebula.dragonship.automation.behaviour.state;

public sealed interface Failed extends ShipBehaviorResult permits Failure, FailureWithReason {
    String message();
}
