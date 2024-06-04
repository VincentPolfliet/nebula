package com.github.skidrunner.fbt.nodes;

import com.github.skidrunner.fbt.BehaviourTreeNode;
import com.github.skidrunner.fbt.BehaviourTreeStatus;
import com.github.skidrunner.fbt.ParentBehaviourTreeNode;

import java.util.Vector;

public class InverterNode implements ParentBehaviourTreeNode {

    private String name;

    private BehaviourTreeNode node;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InverterNode(String name) {
        this.name = name;
    }

    @Override
    public BehaviourTreeStatus tick(float deltaTime) {
        if (node == null) {
            return BehaviourTreeStatus.FAILURE;
        }
        BehaviourTreeStatus status = node.tick(deltaTime);
        switch (status) {
            case SUCCESS:
                return BehaviourTreeStatus.FAILURE;
            case FAILURE:
                return BehaviourTreeStatus.SUCCESS;
            default:
                return status;
        }
    }

    @Override
    public void add(BehaviourTreeNode node) {
        this.node = node;
    }

    @Override
    public BehaviourTreeNode current() {
        return node;
    }

    @Override
    public Vector<BehaviourTreeNode> getNodes() {
        Vector<BehaviourTreeNode> nodes = new Vector<>(1);
        nodes.add(node);

        return nodes;
    }
}
