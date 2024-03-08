package dev.vinpol.torterra;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PredicateLeafTest {

    @Test
    void ifTrueThanOnTrueLeafIsCalledAndCopiesState() {
        Leaf<Object> onTrueLeaf = Torterra.succeed();
        Leaf<Object> onFailLeaf = Torterra.fail();

        PredicateLeaf<Object> predicate = new PredicateLeaf<>(
            (instance) -> true,
            onTrueLeaf,
            onFailLeaf
        );

        predicate.act(new Object());

        assertThat(predicate.isSuccess()).isTrue();
        assertThat(onTrueLeaf.isSuccess()).isTrue();
        assertThat(onFailLeaf.isRunning()).isTrue();
    }

    @Test
    void ifFalseThanOnFalseLeafIsCalledAndCopiesState() {
        Leaf<Object> onTrueLeaf = Torterra.succeed();
        Leaf<Object> onFailLeaf = Torterra.fail();

        PredicateLeaf<Object> predicate = new PredicateLeaf<>(
            (instance) -> false,
            onTrueLeaf,
            onFailLeaf
        );

        predicate.act(new Object());

        assertThat(predicate.isFailure()).isTrue();
        assertThat(onTrueLeaf.isRunning()).isTrue();
        assertThat(onFailLeaf.isFailure()).isTrue();
    }
}
