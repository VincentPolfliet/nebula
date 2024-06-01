package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.models.Ship;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface ShipBehaviour {
    static ShipBehaviour ofResult(ShipBehaviourResult result) {
        return new ShipBehaviour() {
            @Override
            public String getName() {
                return result.toString();
            }

            @Override
            public ShipBehaviourResult update(Ship inShip) {
                return result;
            }

            @Override
            public String toString() {
                return getName();
            }
        };
    }

    static ShipBehaviour succeed() {
        return ofResult(ShipBehaviourResult.success());
    }

    static ShipBehaviour fail() {
        return ofResult(ShipBehaviourResult.failure(FailureReason.FAILURE));
    }

    ShipBehaviourResult update(Ship ship);

    static ShipBehaviour finished() {
        return new FinishedBehaviour();
    }

    static ShipBehaviour ofSupplier(Supplier<ShipBehaviour> supplier) {
        Objects.requireNonNull(supplier);

        return new LazyLoadShipBehaviour(_ -> supplier.get());
    }

    static ShipBehaviour ofFunction(Function<Ship, ShipBehaviour> function) {
        return new LazyLoadShipBehaviour(function);
    }

    default String getName() {
        return this.getClass().getSimpleName();
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
