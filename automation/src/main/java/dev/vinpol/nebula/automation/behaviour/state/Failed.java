package dev.vinpol.nebula.automation.behaviour.state;

public sealed interface Failed extends ShipBehaviourResult permits Failure, FailureWithDetail, FailureWithReason {

}
