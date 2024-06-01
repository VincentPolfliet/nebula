package dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor.fbt;

import com.github.skidrunner.fbt.ParentBehaviourTreeNode;
import com.github.skidrunner.fbt.BehaviourTreeStatus;
import com.github.skidrunner.fbt.BehaviourTreeNode;

import java.util.Vector;

public class OneTickSelectorNode implements ParentBehaviourTreeNode {

    private String name;
    private final Vector<BehaviourTreeNode> nodes;
    private int currentNodeIndex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OneTickSelectorNode(String name) {
        this.name = name;
        this.nodes = new Vector<>();
        this.currentNodeIndex = 0;
    }

    @Override
    public BehaviourTreeStatus tick(float deltaTime) {
        while (currentNodeIndex < nodes.size()) {
            BehaviourTreeNode currentNode = nodes.get(currentNodeIndex);
            BehaviourTreeStatus nodeStatus = currentNode.tick(deltaTime);

            if (nodeStatus == BehaviourTreeStatus.SUCCESS) {
                currentNodeIndex = 0; // Reset for the next run
                return BehaviourTreeStatus.SUCCESS;
            }

            if (nodeStatus == BehaviourTreeStatus.RUNNING) {
                return BehaviourTreeStatus.RUNNING;
            }

            // Move to the next node if the current one fails
            currentNodeIndex++;
        }

        // If all nodes fail
        currentNodeIndex = 0; // Reset for the next run
        return BehaviourTreeStatus.FAILURE;
    }

    @Override
    public void add(BehaviourTreeNode node) {
        nodes.add(node);
    }

    @Override
    public BehaviourTreeNode current() {
        return nodes.get(currentNodeIndex);
    }

    @Override
    public Vector<BehaviourTreeNode> getNodes() {
        return nodes;
    }
}
