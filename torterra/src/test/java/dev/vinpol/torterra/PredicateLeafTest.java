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

        LeafState state = predicate.act(new Object());

        assertThat(state).isEqualTo(LeafState.SUCCESS);
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

        LeafState state = predicate.act(new Object());

        assertThat(state).isEqualTo(LeafState.FAILED);
    }
}
