package dev.vinpol.torterra;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SequenceTest {

    @Test
    void whenLeafSuccessSequenceAlsoSuccess() {
        Leaf<Object> failed = Torterra.succeed();
        Sequence<Object> sequence = new Sequence<>(
            List.of(failed)
        );

        sequence.act(new Object());

        assertThat(sequence.isSuccess()).isTrue();
    }

    @Test
    void whenLeafFailsSequenceAlsoFails() {
        Leaf<Object> failed = Torterra.fail();
        Sequence<Object> sequence = new Sequence<>(
            List.of(failed)
        );

        sequence.act(new Object());

        assertThat(sequence.isFailure()).isTrue();
    }

    @Test
    void whenLeafFailsNoStepsAfterAreExecuted() {
        Leaf<Object> lastStep = new Leaf<Object>() {
            @Override
            public void act(Object instance) {
                succeed();
            }
        };

        Sequence<Object> sequence = new Sequence<>(
            List.of(
                Torterra.succeed(),
                Torterra.fail(),
                lastStep
            )
        );

        sequence.act(new Object());

        assertThat(sequence.isFailure()).isTrue();
        assertThat(lastStep.getState()).isEqualTo(Leaf.LeafState.IDLE);
    }
}
