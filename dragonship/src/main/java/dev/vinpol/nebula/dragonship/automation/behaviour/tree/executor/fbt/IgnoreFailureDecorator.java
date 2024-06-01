package dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor.fbt;

import com.github.skidrunner.fbt.BehaviourTreeNode;
import com.github.skidrunner.fbt.BehaviourTreeStatus;

public class IgnoreFailureDecorator implements BehaviourTreeNode {

    private final BehaviourTreeNode inner;

    public IgnoreFailureDecorator(BehaviourTreeNode inner) {
        this.inner = inner;
    }

    @Override
    public String getName() {
        return "ignore(%s)".formatted(inner.getName());
    }

    @Override
    public BehaviourTreeStatus tick(float deltaTime) {
        BehaviourTreeStatus result = inner.tick(deltaTime);
        return result == BehaviourTreeStatus.FAILURE ? BehaviourTreeStatus.SUCCESS : result;
    }
}
