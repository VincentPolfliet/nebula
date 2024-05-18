package dev.vinpol.torterra;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static dev.vinpol.torterra.Torterra.fail;
import static dev.vinpol.torterra.Torterra.succeed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ComboLeafIteratorTest {
    @Test
    void onSuccess() {
        ComboLeafIterator<Object> iterator = new ComboLeafIterator<>(
            List.of(LeafIterator.singleton(succeed()))
        );

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.act(new Object()).isSuccess()).isTrue();
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    void onFail() {
        ComboLeafIterator<Object> iterator = new ComboLeafIterator<>(
            List.of(LeafIterator.singleton(fail()))
        );

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.act(new Object()).isFailure()).isTrue();
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    void emptyIterator() {
        ComboLeafIterator<Object> iterator = new ComboLeafIterator<>(Collections.emptyList());
        Object object = new Object();

        assertThat(iterator.hasNext()).isFalse();
        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> iterator.act(object));
    }

    @Test
    void multipleIterators() {
        ComboLeafIterator<Object> iterator = new ComboLeafIterator<>(List.of(
            LeafIterator.singleton(succeed()),
            LeafIterator.singleton(fail()),
            LeafIterator.singleton(succeed())
        ));

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.act(new Object()).isSuccess()).isTrue();
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.act(new Object()).isFailure()).isTrue();
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.act(new Object()).isSuccess()).isTrue();
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    void nestedIterators() {
        ComboLeafIterator<Object> iterator = new ComboLeafIterator<>(
            List.of(
                LeafIterator.of(
                    succeed(),
                    fail()
                ),
                LeafIterator.singleton(succeed()),
                LeafIterator.of(
                    fail()
                )
            )
        );

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.act(new Object()).isSuccess()).isTrue();
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.act(new Object()).isFailure()).isTrue();
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.act(new Object()).isSuccess()).isTrue();
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.act(new Object()).isFailure()).isTrue();
        assertThat(iterator.hasNext()).isFalse();
    }
}
