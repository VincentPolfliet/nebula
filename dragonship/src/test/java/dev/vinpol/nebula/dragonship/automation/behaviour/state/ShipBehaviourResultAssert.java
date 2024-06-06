package dev.vinpol.nebula.dragonship.automation.behaviour.state;

import org.assertj.core.api.AbstractAssert;

public class ShipBehaviourResultAssert extends AbstractAssert<ShipBehaviourResultAssert, ShipBehaviourResult> {


    protected ShipBehaviourResultAssert(ShipBehaviourResult actual) {
        super(actual, ShipBehaviourResultAssert.class);
    }

    public static ShipBehaviourResultAssert assertThat(ShipBehaviourResult actual) {
        return new ShipBehaviourResultAssert(actual);
    }

    public ShipBehaviourResultAssert isDone() {
        isNotNull();

        if (!actual.isDone()) {
            failWithMessage("Expected state <%s> but was <%s>", ShipBehaviourResult.done(), actual);
        }

        return this;
    }

    public ShipBehaviourResultAssert isWaitUntil() {
        isNotNull();

        if (!actual.isWaitUntil()) {
            failWithMessage("Expected state <%s> but was <%s>", WaitUntil.class.getSimpleName(), actual);
        }

        return this;
    }

    public ShipBehaviourResultAssert isSuccess() {
        isNotNull();

        if (!actual.isSuccess()) {
            failWithMessage("Expected state <%s> but was <%s>", ShipBehaviourResult.success(), actual);
        }

        return this;
    }
}
