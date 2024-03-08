package dev.vinpol.torterra;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DelegateLeafTest {

    @Test
    void onSucceed() {
        Leaf<Object> innerStep = Torterra.succeed();
        DelegateLeaf<Object> delegate = new DelegateLeaf<>(innerStep);

        delegate.act(new Object());

        assertThat(delegate.isSuccess()).isTrue();
        assertThat(innerStep.isSuccess()).isTrue();
    }


    @Test
    void onFail() {
        Leaf<Object> innerStep = Torterra.fail();
        DelegateLeaf<Object> delegate = new DelegateLeaf<>(innerStep);

        delegate.act(new Object());

        assertThat(delegate.isFailure()).isTrue();
        assertThat(innerStep.isFailure()).isTrue();
    }
}
