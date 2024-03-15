package dev.vinpol.torterra;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DelegateLeafTest {

    @Test
    void onSucceed() {
        Leaf<Object> innerStep = Torterra.succeed();
        DelegateLeaf<Object> delegate = new DelegateLeaf<>(innerStep);

        LeafState result = delegate.act(new Object());

        assertThat(result).isEqualTo(LeafState.SUCCESS);
    }


    @Test
    void onFail() {
        Leaf<Object> innerStep = Torterra.fail();
        DelegateLeaf<Object> delegate = new DelegateLeaf<>(innerStep);

        LeafState state = delegate.act(new Object());

        assertThat(state).isEqualTo(LeafState.FAILED);
    }
}
