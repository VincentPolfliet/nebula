package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.spacetraders.sdk.models.Ship;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface ShipBehaviour {
    static ShipBehaviour ofResult(ShipBehaviourResult result) {
        return (inShip) -> result;
    }

    ShipBehaviourResult update(Ship ship);

    static ShipBehaviour finished() {
        return new FinishedBehaviour();
    }

    static ShipBehaviour ofSupplier(Supplier<ShipBehaviour> supplier) {
        Objects.requireNonNull(supplier);

        return new LazyLoadShipBehaviour(ship -> supplier.get());
    }

    static ShipBehaviour ofFunction(Function<Ship, ShipBehaviour> function) {
        return new LazyLoadShipBehaviour(function);
    }

    class FinishedBehaviour implements ShipBehaviour {

        private FinishedBehaviour() {

        }

        @Override
        public ShipBehaviourResult update(Ship ship) {
            // do nothing
            return ShipBehaviourResult.done();
        }
    }

    class LazyLoadShipBehaviour implements ShipBehaviour {

        private final Function<Ship, ShipBehaviour> shipBehaviourFunction;
        private ShipBehaviour loadedBehaviour = null;

        private LazyLoadShipBehaviour(Function<Ship, ShipBehaviour> shipBehaviourFunction) {
            this.shipBehaviourFunction = Objects.requireNonNull(shipBehaviourFunction);
        }

        @Override
        public ShipBehaviourResult update(Ship ship) {
            if (loadedBehaviour == null) {
                loadedBehaviour = shipBehaviourFunction.apply(ship);
            }

            return loadedBehaviour.update(ship);
        }
    }

}
