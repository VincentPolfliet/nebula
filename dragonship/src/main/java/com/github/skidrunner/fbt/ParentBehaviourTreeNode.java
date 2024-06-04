package com.github.skidrunner.fbt;

import java.util.Vector;

public interface ParentBehaviourTreeNode extends BehaviourTreeNode {

    void add(BehaviourTreeNode node);

    BehaviourTreeNode current();

    Vector<BehaviourTreeNode> getNodes();
}
