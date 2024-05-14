package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.Done;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.Failed;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.Success;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.StatefulLeaf;
import dev.vinpol.torterra.Leaf;
import dev.vinpol.torterra.Torterra;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipSequenceBehaviourTest {

    @Test
    void treeIsNullThrowsException() {
        NullPointerException exception = Assertions.catchNullPointerException(() -> new ShipSequenceBehaviour(null));

        assertThat(exception).isNotNull();
    }

    @Test
    void shipIsNullThrowsException() {
        ShipSequenceBehaviour sut = new ShipSequenceBehaviour(Collections.emptyList());
        NullPointerException exception = Assertions.catchNullPointerException(() -> sut.update(null));

        assertThat(exception).isNotNull();
    }

    @Test
    void returnsDoneWhenFinished() {
        ShipSequenceBehaviour sut = new ShipSequenceBehaviour(Collections.emptyList());

        Ship ship = new Ship();
        ShipBehaviourResult result = sut.update(ship);

        assertThat(result).isInstanceOf(Done.class);
        assertThat(result.isDone()).isTrue();
    }

    @Test
    void returnsTrueAndDoneWithOneSuccessStep() {
        List<Leaf<Ship>> tree = List.of(
            Torterra.succeed()
        );

        ShipSequenceBehaviour sut = new ShipSequenceBehaviour(tree);

        Ship ship = new Ship();
        ShipBehaviourResult firstTick = sut.update(ship);

        assertThat(firstTick).isInstanceOf(Success.class);
        assertThat(firstTick.isSuccess()).isTrue();
        assertThat(firstTick.isFailure()).isFalse();

        ShipBehaviourResult secondTick = sut.update(ship);

        assertThat(secondTick).isInstanceOf(Done.class);
        assertThat(secondTick.isDone()).isTrue();
    }

    @Test
    void returnsFalseAndDoneWithOneFailedStep() {
        List<Leaf<Ship>> tree = List.of(
            Torterra.fail()
        );

        ShipSequenceBehaviour sut = new ShipSequenceBehaviour(tree);

        Ship ship = new Ship();
        ShipBehaviourResult firstTick = sut.update(ship);

        assertThat(firstTick).isInstanceOf(Failed.class);
        assertThat(firstTick.isSuccess()).isFalse();
        assertThat(firstTick.isFailure()).isTrue();

        ShipBehaviourResult secondTick = sut.update(ship);

        assertThat(secondTick).isInstanceOf(Done.class);
        assertThat(secondTick.isDone()).isTrue();
    }

    @Test
    void returnsDoneWhenStepIsStillRunning() {
        List<Leaf<Ship>> tree = List.of(
            new StatefulLeaf<Ship>() {
                @Override
                public void doAct(Ship instance) {

                }
            }
        );

        ShipSequenceBehaviour sut = new ShipSequenceBehaviour(tree);

        Ship ship = new Ship();
        ShipBehaviourResult result = sut.update(ship);

        assertThat(result).isInstanceOf(Success.class);
        assertThat(result.isSuccess()).isTrue();
    }
}
