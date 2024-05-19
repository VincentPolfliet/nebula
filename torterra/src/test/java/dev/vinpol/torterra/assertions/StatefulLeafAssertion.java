package dev.vinpol.torterra.assertions;

import dev.vinpol.torterra.StatefulLeaf;
import org.assertj.core.api.AbstractAssert;

import java.util.function.Predicate;

public class StatefulLeafAssertion<T> extends AbstractAssert<StatefulLeafAssertion<T>, StatefulLeaf<T>> {

    protected StatefulLeafAssertion(StatefulLeaf<T> leaf) {
        super(leaf, StatefulLeafAssertion.class);
    }

    public static <T> StatefulLeafAssertion<T> assertLeaf(StatefulLeaf<T> leaf) {
        return new StatefulLeafAssertion<>(leaf);
    }

    public StatefulLeafAssertion<T> isSuccess() {
        return assertState(LeafStateType.SUCCESS);
    }

    public StatefulLeafAssertion<T> isFailure() {
        return assertState(LeafStateType.FAILED);
    }

    public StatefulLeafAssertion<T> isRunning() {
        return assertState(LeafStateType.RUNNING);
    }

    private StatefulLeafAssertion<T> assertState(LeafStateType type) {
        isNotNull();

        if (!type.check(actual)) {
            failWithMessage("Expected state to be <%s>", type);
        }

        return this;
    }

    private enum LeafStateType {
        RUNNING(StatefulLeaf::isRunning),
        SUCCESS(StatefulLeaf::isSuccess),
        FAILED(StatefulLeaf::isFailure);


        private final Predicate<StatefulLeaf<?>> check;

        LeafStateType(Predicate<StatefulLeaf<?>> check) {
            this.check = check;
        }

        public boolean check(StatefulLeaf<?> state) {
            return this.check.test(state);
        }
    }
}
