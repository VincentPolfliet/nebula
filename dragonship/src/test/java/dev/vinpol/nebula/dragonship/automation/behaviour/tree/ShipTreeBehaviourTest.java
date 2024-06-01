package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.Done;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.spacetraders.sdk.models.MotherShip;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Collections;

import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourSequence.sequence;
import static org.assertj.core.api.Assertions.assertThat;

class ShipTreeBehaviourTest {

    @Test
    void treeIsNullThrowsException() {
        NullPointerException exception = Assertions.catchNullPointerException(() -> new ShipTreeBehaviour((ShipBehaviour) null));

        assertThat(exception).isNotNull();
    }

    @Test
    void shipIsNullThrowsException() {
        ShipTreeBehaviour sut = new ShipTreeBehaviour(Collections.emptyList());
        NullPointerException exception = Assertions.catchNullPointerException(() -> sut.update(null));

        assertThat(exception).isNotNull();
    }

    @Test
    void returnsDoneWhenFinished() {
        ShipTreeBehaviour sut = new ShipTreeBehaviour(Collections.emptyList());

        Ship ship = new Ship();
        ShipBehaviourResult result = sut.update(ship);

        assertThat(result).isInstanceOf(Done.class);
        assertThat(result.isDone()).isTrue();
    }

    @Test
    void returnsTrueAndDoneWithOneSuccessStep() {
        ShipTreeBehaviour sut = new ShipTreeBehaviour(ShipBehaviour.succeed());

        Ship ship = new Ship();
        ShipBehaviourResult tick = sut.update(ship);
        assertThat(tick.isSuccess()).isTrue();

        ShipBehaviourResult secondTick = sut.update(ship);
        assertThat(secondTick.isDone()).isTrue();
    }

    @Test
    void returnsFalseAndDoneWithOneFailedStep() {
        ShipTreeBehaviour sut = new ShipTreeBehaviour(ShipBehaviour.fail());

        Ship ship = new Ship();
        assertThat(sut.update(ship).isFailure()).isTrue();
        assertThat(sut.update(ship).isFailure()).isTrue();
    }

    @Test
    void testSequence() {
        Ship ship = MotherShip.excavator();

        ShipTreeBehaviour sut = new ShipTreeBehaviour(
            sequence(
                "sequence",
                ShipBehaviour.fail(),
                ShipBehaviour.succeed()
            ),
            ShipBehaviour.succeed()
        );

        ShipBehaviourResult firstTick = sut.update(ship);
        assertThat(firstTick.isSuccess()).isTrue();

        ShipBehaviourResult secondTick = sut.update(ship);
        assertThat(secondTick.isSuccess()).isTrue();

        ShipBehaviourResult thirdTick = sut.update(ship);
        assertThat(thirdTick.isDone()).isTrue();
    }

    @Test
    void testSequence2() {
        Ship ship = MotherShip.excavator();

        ShipTreeBehaviour sut = new ShipTreeBehaviour(
            sequence(
                "sequence2",
                ShipBehaviour.fail(),
                ShipBehaviour.succeed()
            )
        );

        assertThat(sut.update(ship).isSuccess()).isTrue();
        assertThat(sut.update(ship).isDone()).isTrue();
    }

    @Test
    void testWaitUntil() {
        Ship ship = MotherShip.excavator();
        OffsetDateTime timestamp = OffsetDateTime.now();

        ShipTreeBehaviour sut = new ShipTreeBehaviour(ShipBehaviour.ofResult(ShipBehaviourResult.waitUntil(timestamp)));

        ShipBehaviourResult tick = sut.update(ship);
        assertThat(tick.isWaitUntil()).isTrue();
        assertThat(tick).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.timestamp()).isEqualTo(timestamp);
        });
    }
}
