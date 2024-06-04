package com.github.skidrunner.fbt.nodes;

import com.github.skidrunner.fbt.BehaviourTreeNode;
import com.github.skidrunner.fbt.BehaviourTreeStatus;
import com.github.skidrunner.fbt.ParentBehaviourTreeNode;

import java.util.Vector;

public class SelectorNode implements ParentBehaviourTreeNode {

    private String name;
    private final Vector<BehaviourTreeNode> nodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public SelectorNode(String name) {
        this.name = name;
        this.nodes = new Vector<>();
    }

    @Override
    public BehaviourTreeStatus tick(float deltaTime) {
        for (BehaviourTreeNode node : nodes) {
            BehaviourTreeStatus nodeStatus = node.tick(deltaTime);
            if (nodeStatus != BehaviourTreeStatus.FAILURE) {
                return nodeStatus;
            }
        }
        return BehaviourTreeStatus.FAILURE;
    }

    @Override
    public void add(BehaviourTreeNode node) {
        this.nodes.add(node);
    }

    @Override
    public BehaviourTreeNode current() {
        return null;
    }

    @Override
    public Vector<BehaviourTreeNode> getNodes() {
        return nodes;
    }
}
