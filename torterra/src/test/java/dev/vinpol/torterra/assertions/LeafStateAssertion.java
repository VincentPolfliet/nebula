package dev.vinpol.torterra.assertions;

import dev.vinpol.torterra.FailedState;
import dev.vinpol.torterra.LeafState;
import dev.vinpol.torterra.RunningState;
import org.assertj.core.api.AbstractAssert;

import java.util.function.Predicate;

/**
 * Assertion class for testing instances of {@link LeafState}.
 *
 *
 * <pre>
 * Example usage:
 * {@code
 * LeafState state = getLeafState();
 * LeafStateAssertion.assertThat(state).isSuccess();
 * }
 * </pre>
 */
public class LeafStateAssertion extends AbstractAssert<LeafStateAssertion, LeafState> {
    protected LeafStateAssertion(LeafState leafState) {
        super(leafState, LeafStateAssertion.class);
    }

    public static LeafStateAssertion assertThat(LeafState state) {
        return new LeafStateAssertion(state);
    }

    public LeafStateAssertion isSuccess() {
        return assertState(LeafStateType.SUCCESS);
    }

    public LeafStateAssertion isFailure() {
        return assertState(LeafStateType.FAILED);
    }

    public LeafStateAssertion isRunning() {
        return assertState(LeafStateType.RUNNING);
    }

    private LeafStateAssertion assertState(LeafStateType type) {
        isNotNull();

        if (!type.check(actual)) {
            failWithMessage("Expected state to be <%s> but was <%s>", type, actual.getClass().getSimpleName());
        }

        return this;
    }

    private enum LeafStateType {
        RUNNING(LeafState::isRunning),
        SUCCESS(LeafState::isSuccess),
        FAILED(LeafState::isFailure);


        private final Predicate<LeafState> check;

        LeafStateType(Predicate<LeafState> check) {
            this.check = check;
        }

        public boolean check(LeafState state) {
            return this.check.test(state);
        }
    }
}
