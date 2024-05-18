package dev.vinpol.nebula.dragonship.automation.behaviour.state;

record FailureWithReason(FailureReason reason) implements Failed {

    @Override
    public String message() {
        return reason.name();
    }
}
