package com.github.skidrunner.fbt.nodes;

import com.github.skidrunner.fbt.BehaviourTreeNode;
import com.github.skidrunner.fbt.BehaviourTreeStatus;
import com.github.skidrunner.fbt.ParentBehaviourTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

public class SequenceNode implements ParentBehaviourTreeNode {

    private Logger logger = LoggerFactory.getLogger(SequenceNode.class);

    private String name;
    private final Vector<BehaviourTreeNode> nodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SequenceNode(String name) {
        this.name = name;
        this.nodes = new Vector<>();
    }

    @Override
    public BehaviourTreeStatus tick(float deltaTime) {
        for (BehaviourTreeNode node : nodes) {
            BehaviourTreeStatus nodeStatus = node.tick(deltaTime);

            if (nodeStatus != BehaviourTreeStatus.SUCCESS) {
                return nodeStatus;
            }
        }

        return BehaviourTreeStatus.SUCCESS;
    }

    @Override
    public void add(BehaviourTreeNode node) {
        nodes.add(node);
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
