package dev.vinpol.torterra;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        leafs.add(spy(Torterra.fail()));
        leafs.add(spy(Torterra.succeed()));
        leafs.add(spy(Torterra.succeed()));
        leafs.add(spy(Torterra.fail()));

        Selector<Object> selector = new Selector<>(leafs);

        Object instance = new Object();
        selector.act(instance);

        assertThat(selector.isSuccess()).isTrue();

        verify(leafs.get(0)).act(instance);
        verify(leafs.get(1)).act(instance);
        verifyNoInteractions(leafs.get(2));
        verifyNoInteractions(leafs.get(3));
    }

    @Test
    void actSucceed() {
        Selector<Object> selector = new Selector<>(List.of(Torterra.succeed()));

        Object instance = new Object();
        selector.act(instance);

        assertThat(selector.isSuccess()).isTrue();
        assertThat(selector.isFailure()).isFalse();
    }

    @Test
    void actFailed() {
        Selector<Object> selector = new Selector<>(List.of(Torterra.fail()));

        Object instance = new Object();
        selector.act(instance);

        assertThat(selector.isSuccess()).isFalse();
        assertThat(selector.isFailure()).isTrue();
    }
}
