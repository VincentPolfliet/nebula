package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.Done;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.Failed;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.Success;
import dev.vinpol.spacetraders.sdk.models.MotherShip;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipNavStatus;
import dev.vinpol.torterra.Leaf;
import dev.vinpol.torterra.StatefulLeaf;
import dev.vinpol.torterra.Torterra;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static dev.vinpol.torterra.Torterra.selector;
import static dev.vinpol.torterra.Torterra.sequence;
import static org.assertj.core.api.Assertions.assertThat;

class ShipSequenceBehaviourTest {

    @Test
    void treeIsNullThrowsException() {
        NullPointerException exception = Assertions.catchNullPointerException(() -> new ShipSequenceBehaviour((Leaf<Ship>) null));

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
        ShipSequenceBehaviour sut = new ShipSequenceBehaviour(Torterra.succeed());

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
        ShipSequenceBehaviour sut = new ShipSequenceBehaviour(Torterra.fail());

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
        ShipSequenceBehaviour sut = new ShipSequenceBehaviour(new StatefulLeaf<Ship>() {
            @Override
            public void doAct(Ship instance) {

            }
        });

        Ship ship = new Ship();
        ShipBehaviourResult result = sut.update(ship);

        assertThat(result).isInstanceOf(Success.class);
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void testSequence() {
        ShipSequenceBehaviour behaviour = new ShipSequenceBehaviour(
            sequence(
                Torterra.fail(),
                Torterra.succeed()
            ),
            Torterra.succeed()
        );

        Ship ship = MotherShip.excavator()
            .withNav(nav -> {
                nav.setStatus(ShipNavStatus.IN_ORBIT);
            });

        // dock check
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // sequence step
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // done
        assertThat(behaviour.update(ship).isDone()).isTrue();
    }

    @Test
    void testSelector() {
        ShipSequenceBehaviour behaviour = new ShipSequenceBehaviour(
            selector(
                Torterra.fail(),
                Torterra.fail(),
                Torterra.succeed()
            )
        );

        Ship ship = MotherShip.excavator();

        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(behaviour.update(ship).isDone()).isTrue();
    }
}
