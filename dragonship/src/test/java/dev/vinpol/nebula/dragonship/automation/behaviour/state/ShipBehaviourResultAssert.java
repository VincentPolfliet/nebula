package dev.vinpol.nebula.dragonship.automation.behaviour.state;

import org.assertj.core.api.AbstractAssert;

public class ShipBehaviourResultAssert extends AbstractAssert<ShipBehaviourResultAssert, ShipBehaviorResult> {


    protected ShipBehaviourResultAssert(ShipBehaviorResult actual) {
        super(actual, ShipBehaviourResultAssert.class);
    }

    public static ShipBehaviourResultAssert assertThat(ShipBehaviorResult actual) {
        return new ShipBehaviourResultAssert(actual);
    }

    public ShipBehaviourResultAssert isDone() {
        isNotNull();

        if (!actual.isDone()) {
            failWithMessage("Expected state <%s> but was <%s>", ShipBehaviorResult.done(), actual);
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
            failWithMessage("Expected state <%s> but was <%s>", ShipBehaviorResult.success(), actual);
        }

        return this;
    }
}
