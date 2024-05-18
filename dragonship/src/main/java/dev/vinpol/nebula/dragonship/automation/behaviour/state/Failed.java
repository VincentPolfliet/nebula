package dev.vinpol.nebula.dragonship.automation.behaviour.state;

public sealed interface Failed extends ShipBehaviourResult permits Failure, FailureWithReason {
    String message();
}
