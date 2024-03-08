package dev.vinpol.nebula.automation.tree;

import dev.vinpol.nebula.automation.behaviour.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.Leaf;
import dev.vinpol.torterra.Torterra;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShipTreeBehaviourTest {

    @Test
    void treeIsNullThrowsException() {
        NullPointerException exception = Assertions.catchNullPointerException(() -> new ShipTreeBehaviour(null));

        assertThat(exception).isNotNull();
    }

    @Test
    void shipIsNullThrowsException() {
        ShipTreeBehaviour sut = new ShipTreeBehaviour(Torterra.plant());
        NullPointerException exception = Assertions.catchNullPointerException(() -> sut.update(null));

        assertThat(exception).isNotNull();
    }

    @Test
    void returnsDoneWhenFinished() {
        Torterra.Tree<Ship> tree = Torterra.plant();

        ShipTreeBehaviour sut = new ShipTreeBehaviour(tree);

        Ship ship = new Ship();
        ShipBehaviourResult result = sut.update(ship);

        assertThat(result).isInstanceOf(ShipBehaviourResult.Done.class);
        assertThat(result.isDone()).isTrue();
    }

    @Test
    void returnsTrueAndDoneWithOneSuccessStep() {
        Torterra.Tree<Ship> tree = Torterra.plant(
            Torterra.succeed()
        );

        ShipTreeBehaviour sut = new ShipTreeBehaviour(tree);

        Ship ship = new Ship();
        ShipBehaviourResult firstTick = sut.update(ship);

        assertThat(firstTick).isInstanceOf(ShipBehaviourResult.Success.class);
        assertThat(firstTick.isSuccess()).isTrue();
        assertThat(firstTick.isFailure()).isFalse();

        ShipBehaviourResult secondTick = sut.update(ship);

        assertThat(secondTick).isInstanceOf(ShipBehaviourResult.Done.class);
        assertThat(secondTick.isDone()).isTrue();
    }

    @Test
    void returnsFalseAndDoneWithOneFailedStep() {
        Torterra.Tree<Ship> tree = Torterra.plant(
            Torterra.fail()
        );

        ShipTreeBehaviour sut = new ShipTreeBehaviour(tree);

        Ship ship = new Ship();
        ShipBehaviourResult firstTick = sut.update(ship);

        assertThat(firstTick).isInstanceOf(ShipBehaviourResult.Failed.class);
        assertThat(firstTick.isSuccess()).isFalse();
        assertThat(firstTick.isFailure()).isTrue();

        ShipBehaviourResult secondTick = sut.update(ship);

        assertThat(secondTick).isInstanceOf(ShipBehaviourResult.Done.class);
        assertThat(secondTick.isDone()).isTrue();
    }

    @Test
    void returnsDoneWhenStepIsStillRunning() {
        Torterra.Tree<Ship> tree = Torterra.plant(
            new Leaf<Ship>() {
                @Override
                public void act(Ship instance) {

                }
            }
        );

        ShipTreeBehaviour sut = new ShipTreeBehaviour(tree);

        Ship ship = new Ship();
        ShipBehaviourResult result = sut.update(ship);

        assertThat(result).isInstanceOf(ShipBehaviourResult.Done.class);
        assertThat(result.isDone()).isTrue();
    }


}
