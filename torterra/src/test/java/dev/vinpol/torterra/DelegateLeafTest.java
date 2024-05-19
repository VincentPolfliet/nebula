package dev.vinpol.torterra;

import org.junit.jupiter.api.Test;

import static dev.vinpol.torterra.assertions.LeafStateAssertion.assertState;

class DelegateLeafTest {

    @Test
    void onSucceed() {
        Leaf<Object> innerStep = Torterra.succeed();
        DelegateLeaf<Object> delegate = new DelegateLeaf<>(innerStep);

        LeafState result = delegate.act(new Object());

        assertState(result).isSuccess();
    }


    @Test
    void onFail() {
        Leaf<Object> innerStep = Torterra.fail();
        DelegateLeaf<Object> delegate = new DelegateLeaf<>(innerStep);

        LeafState state = delegate.act(new Object());

        assertState(state).isFailure();
    }
}
