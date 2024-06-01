package dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor.fbt;

import com.github.skidrunner.fbt.BehaviourTreeNode;
import com.github.skidrunner.fbt.BehaviourTreeStatus;
import com.github.skidrunner.fbt.ParentBehaviourTreeNode;

import java.util.Vector;

public class IgnoreParentFailureDecorator implements ParentBehaviourTreeNode {
    private final ParentBehaviourTreeNode node;

    public IgnoreParentFailureDecorator(ParentBehaviourTreeNode node) {
        this.node = node;
    }

    @Override
    public String getName() {
        return "ignore(%s)".formatted(node.getName());
    }

    @Override
    public BehaviourTreeStatus tick(float deltaTime) {
        BehaviourTreeStatus result = node.tick(deltaTime);
        return result == BehaviourTreeStatus.FAILURE ? BehaviourTreeStatus.SUCCESS : result;
    }

    @Override
    public void add(BehaviourTreeNode node) {
        this.node.add(node);
    }

    @Override
    public BehaviourTreeNode current() {
        return node;
    }

    @Override
    public Vector<BehaviourTreeNode> getNodes() {
        return this.node.getNodes();
    }
}
