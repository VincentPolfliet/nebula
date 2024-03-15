package dev.vinpol.torterra;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dev.vinpol.torterra.Torterra.fail;
import static dev.vinpol.torterra.Torterra.succeed;
import static org.assertj.core.api.Assertions.assertThat;

class SelectorTest {

    @Test
    void interableIsNull() {
        Leaf<?> leaf = null;
        NullPointerException exception = Assertions.catchNullPointerException(() -> new Selector<>(null));

        assertThat(exception).isNotNull();
    }

    @Test
    void actSucceedOnFirstSuccess() {
        List<Leaf<Object>> leafs = new ArrayList<>();
        leafs.add(fail());
        leafs.add(succeed());
        leafs.add(succeed());
        leafs.add(fail());

        Selector<Object> selector = new Selector<>(leafs);

        Object instance = new Object();

        selector.act(instance);
        assertThat(selector.isRunning()).isTrue();

        selector.act(instance);
        assertThat(selector.isSuccess()).isTrue();

        selector.act(instance);
        assertThat(selector.isSuccess()).isTrue();

        selector.act(instance);
        assertThat(selector.isSuccess()).isTrue();
    }

    @Test
    void actSucceed() {
        Selector<Object> selector = new Selector<>(List.of(succeed()));

        Object instance = new Object();
        selector.act(instance);

        assertThat(selector.isSuccess()).isTrue();
        assertThat(selector.isFailure()).isFalse();
    }

    @Test
    void actFailed() {
        Selector<Object> selector = new Selector<>(List.of(fail()));

        Object instance = new Object();
        selector.act(instance);

        assertThat(selector.isSuccess()).isFalse();
        assertThat(selector.isFailure()).isTrue();
    }
}
