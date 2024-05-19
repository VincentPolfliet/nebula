package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.torterra.LeafState;

public record ShipBehaviourLeafState(ShipBehaviourResult result) implements LeafState {
    @Override
    public boolean isFailure() {
        return LeafState.super.isFailure();
    }

    @Override
    public boolean isSuccess() {
        return LeafState.super.isSuccess();
    }

    @Override
    public boolean isRunning() {
        return LeafState.super.isRunning();
    }
}
