package dev.vinpol.torterra;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static dev.vinpol.torterra.Torterra.succeed;
import static dev.vinpol.torterra.assertions.LeafStateAssertion.assertState;
import static dev.vinpol.torterra.assertions.StatefulLeafAssertion.assertLeaf;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoInteractions;

class SequenceTest {

    @Test
    void whenLeafSuccessSequenceAlsoSuccess() {
        Sequence<Object> sequence = new Sequence<>(
            List.of(succeed())
        );

        LeafState state = sequence.act(new Object());

        assertState(state).isSuccess();
        assertLeaf(sequence).isSuccess();
    }

    @Test
    void whenLeafFailsSequenceAlsoFails() {
        Leaf<Object> failed = Torterra.fail();
        Sequence<Object> sequence = new Sequence<>(
            List.of(failed)
        );

        LeafState result = sequence.act(new Object());

        assertState(result).isFailure();
        assertLeaf(sequence).isFailure();
    }

    @Test
    void whenAllLeafSuccessSequenceAlsoSuccess() {
        Sequence<Object> sequence = new Sequence<>(
            List.of(succeed(), succeed(), succeed())
        );

        assertState(sequence.act(new Object())).isRunning();
        assertLeaf(sequence).isRunning();

        assertState(sequence.act(new Object())).isRunning();
        assertLeaf(sequence).isRunning();

        assertState(sequence.act(new Object())).isSuccess();

        // repeats final state
        assertState(sequence.act(new Object())).isSuccess();
        assertLeaf(sequence).isSuccess();
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

        assertState(sequence.act(new Object())).isRunning();
        assertLeaf(sequence).isRunning();

        assertState(sequence.act(new Object())).isFailure();

        // repeats final state
        assertState(sequence.act(new Object())).isFailure();
        assertLeaf(sequence).isFailure();

        verifyNoInteractions(lastStep);
    }

    @Test
    void asLeafIterator() {
        Sequence<Object> sequence = new Sequence<>(
            List.of(
                succeed(),
                Torterra.fail(),
                _ -> LeafState.running()
            )
        );

        LeafIterator<Object> leafIterator = sequence.leafIterator();

        Object o = new Object();

        assertThat(leafIterator.hasNext()).isTrue();
        assertState(leafIterator.act(o)).isRunning();

        assertThat(leafIterator.hasNext()).isTrue();
        assertState(leafIterator.act(o)).isFailure();

        assertThat(leafIterator.hasNext()).isFalse();

        assertThatThrownBy(() -> leafIterator.act(o)).isInstanceOf(NoSuchElementException.class);
    }

}
