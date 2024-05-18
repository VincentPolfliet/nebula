package dev.vinpol.torterra;

import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.vinpol.torterra.Torterra.succeed;
import static dev.vinpol.torterra.assertions.LeafStateAssertion.assertThat;
import static org.mockito.Mockito.*;

class SequenceTest {

    @Test
    void whenLeafSuccessSequenceAlsoSuccess() {
        Sequence<Object> sequence = new Sequence<>(
            List.of(succeed())
        );

        LeafState state = sequence.act(new Object());

        assertThat(state).isSuccess();
    }

    @Test
    void whenLeafFailsSequenceAlsoFails() {
        Leaf<Object> failed = Torterra.fail();
        Sequence<Object> sequence = new Sequence<>(
            List.of(failed)
        );

        LeafState result = sequence.act(new Object());

        assertThat(result).isFailure();
    }

    @Test
    void whenAllLeafSuccessSequenceAlsoSuccess() {
        Sequence<Object> sequence = new Sequence<>(
            List.of(succeed(), succeed(), succeed())
        );

        assertThat(sequence.act(new Object())).isRunning();
        assertThat(sequence.act(new Object())).isRunning();
        assertThat(sequence.act(new Object())).isSuccess();

        // repeats final state
        assertThat(sequence.act(new Object())).isSuccess();
    }

    @Test
    void whenLeafFailsNoStepsAfterAreExecuted() {
        Leaf<Object> lastStep = spy(new Leaf<Object>() {
            @Override
            public LeafState act(Object instance) {
                return LeafState.running();
            }
        });

        Sequence<Object> sequence = new Sequence<>(
            List.of(
                succeed(),
                Torterra.fail(),
                lastStep
            )
        );

        assertThat(sequence.act(new Object())).isRunning();
        assertThat(sequence.act(new Object())).isFailure();

        // repeats final state
        assertThat(sequence.act(new Object())).isFailure();

        verifyNoInteractions(lastStep);
    }


}
