package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public record ShipBehaviourSequence(String name,
                                    List<ShipBehaviour> behaviours) implements ShipBehaviour, Iterable<ShipBehaviour>, ShipBehaviourFactory {


    public static ShipBehaviourSequence sequence(String name, ShipBehaviour... shipBehaviours) {
        return new ShipBehaviourSequence(name, List.of(shipBehaviours));
    }

    public static ShipBehaviourSequence sequence(ShipBehaviour... shipBehaviours) {
        return sequence(List.of(shipBehaviours));
    }

    public static ShipBehaviourSequence sequence(List<ShipBehaviour> shipBehaviours) {
        return new ShipBehaviourSequence("sequence-" + UUID.randomUUID(), shipBehaviours);
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public ShipBehaviourResult update(Ship ship) {
        throw new RuntimeException();
    }

    @NotNull
    @Override
    public Iterator<ShipBehaviour> iterator() {
        return behaviours.iterator();
    }

    @Override
    public ShipBehaviour create() {
        return this;
    }
}
