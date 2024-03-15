package dev.vinpol.torterra;

import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.vinpol.torterra.Torterra.succeed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class SequenceTest {

    @Test
    void whenLeafSuccessSequenceAlsoSuccess() {
        Sequence<Object> sequence = new Sequence<>(
            List.of(succeed())
        );

        LeafState state = sequence.act(new Object());

        assertThat(state).isEqualTo(LeafState.SUCCESS);
    }

    @Test
    void whenLeafFailsSequenceAlsoFails() {
        Leaf<Object> failed = Torterra.fail();
        Sequence<Object> sequence = new Sequence<>(
            List.of(failed)
        );

        LeafState result = sequence.act(new Object());

        assertThat(result).isEqualTo(LeafState.FAILED);
    }

    @Test
    void whenAllLeafSuccessSequenceAlsoSuccess() {
        Sequence<Object> sequence = new Sequence<>(
            List.of(succeed(), succeed(), succeed())
        );

        assertThat(sequence.act(new Object())).isEqualTo(LeafState.RUNNING);
        assertThat(sequence.act(new Object())).isEqualTo(LeafState.RUNNING);
        assertThat(sequence.act(new Object())).isEqualTo(LeafState.SUCCESS);
    }

    @Test
    void whenLeafFailsNoStepsAfterAreExecuted() {
        Leaf<Object> lastStep = mock(Leaf.class);

        Sequence<Object> sequence = new Sequence<>(
            List.of(
                succeed(),
                Torterra.fail(),
                lastStep
            )
        );

        assertThat(sequence.act(new Object())).isEqualTo(LeafState.RUNNING);
        assertThat(sequence.act(new Object())).isEqualTo(LeafState.FAILED);

        verifyNoInteractions(lastStep);
    }
}
