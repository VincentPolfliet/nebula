package com.github.skidrunner.fbt;

public interface BehaviourTreeNode {
    String getName();

    BehaviourTreeStatus tick(float deltaTime);
}
