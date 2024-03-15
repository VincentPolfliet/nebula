package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.behaviour.tree.ShipBehaviourRefLeaf;
import dev.vinpol.nebula.automation.behaviour.tree.ShipSequenceBehaviour;
import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import dev.vinpol.torterra.IterableLeaf;
import dev.vinpol.torterra.Leaf;
import dev.vinpol.torterra.TorterraUtils;

import java.util.List;

public interface BehaviourFactoryRegistry {

    MiningBehaviourFactory miningAutomation(SystemSymbol system, WaypointType waypointType);

    ExtractionBehaviourFactory extraction();

    NavigateBehaviourFactory navigateAutomation(WaypointSymbol waypointSymbol);

    OrbitBehaviourFactory orbitAutomation();

    DockBehaviourFactory dock();

    RefuelBehaviour refuel();

    default ShipSequenceBehaviour sequenceOf(List<Leaf<Ship>> leaves) {
        inject(leaves, this);
        return new ShipSequenceBehaviour(leaves);
    }

    private static void inject(Iterable<Leaf<Ship>> leaves, BehaviourFactoryRegistry registry) {
        for (Leaf<Ship> leaf : leaves) {
            Leaf<Ship> unwrapped = TorterraUtils.unwrap(leaf);

            if (unwrapped instanceof IterableLeaf<Ship> iterable) {
                inject(iterable, registry);
            } else if (unwrapped instanceof ShipBehaviourRefLeaf ref) {
                ref.setBehaviourFactory(registry);
            }
        }
    }
}
