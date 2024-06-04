package dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor.fbt;

import com.github.skidrunner.fbt.BehaviourTreeBuilder;
import com.github.skidrunner.fbt.BehaviourTreeNode;
import com.github.skidrunner.fbt.BehaviourTreeStatus;
import com.github.skidrunner.fbt.ParentBehaviourTreeNode;
import com.github.skidrunner.fbt.nodes.ActionNode;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourSequence;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor.ShipBehaviourTree;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.executor.ShipBehaviourTreeFactory;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ShipFBTFactory implements ShipBehaviourTreeFactory {

    @Override
    public ShipBehaviourTree execute(List<ShipBehaviour> behaviours) {
        return new ShipFBT(behaviours);
    }

    private final static class ShipFBT implements ShipBehaviourTree, ShipBehaviour {

        private final Logger logger = LoggerFactory.getLogger(ShipFBT.class);

        private final List<ShipBehaviour> behaviours;
        private final BehaviourTreeBuilder treeBuilder = new BehaviourTreeBuilder();

        private int tick;
        private BehaviourTreeNode tree;
        private BehaviourTreeState state;

        public ShipFBT(List<ShipBehaviour> behaviours) {
            this.behaviours = behaviours;
        }

        private BehaviourTreeStatus tickUntilNextBehaviour(Ship ship) {
            BehaviourTreeStatus latestResult = null;

            do {
                logTree(tree);
                latestResult = tree.tick(++tick);
            } while (state.getCurrentStep().isEmpty());

            logger.debug("tickUntilNextBehaviour: nextBehaviour: {}", state.getCurrentStep().get().getName());

            return latestResult;
        }

        private void mapOnTree(BehaviourTreeBuilder tree, Iterable<ShipBehaviour> leaves, BehaviourTreeState state) {
            tree.start(new OneTickSequenceNode("main"));

            tree.addAction("main-start", _ -> {
                state.setTreeStarted();
                state.setCurrentStep(null);
                return BehaviourTreeStatus.SUCCESS;
            });

            for (ShipBehaviour leaf : leaves) {
                if (leaf instanceof ShipBehaviourSequence sequence) {
                    mapSequence(tree, sequence, state);
                } else {
                    tree.add(new ActionNode(leaf.getName(), new BehaviourAdapter(leaf, state)));
                }
            }

            tree.addAction("main-end", _ -> {
                state.setCurrentStep(null);
                state.setTreeEnded();
                return BehaviourTreeStatus.SUCCESS;
            });

            tree.addAction("done", new BehaviourAdapter(ShipBehaviour.finished(), state));

            tree.end();
        }

        private void mapSequence(BehaviourTreeBuilder tree, ShipBehaviourSequence sequence, BehaviourTreeState state) {
            String sequenceName = sequence.getName();
            tree.start(new IgnoreParentFailureDecorator(new OneTickSequenceNode(sequenceName)));

            tree.addAction("seq-" + sequenceName + "-start", _ -> {
                state.setCurrentStep(null);
                state.setSequenceStarted();
                return BehaviourTreeStatus.SUCCESS;
            });

            for (ShipBehaviour behaviour : sequence.behaviours()) {
                if (behaviour instanceof ShipBehaviourSequence nestedSequence) {
                    mapSequence(tree, nestedSequence, state);
                } else {
                    tree.add(new ActionNode(behaviour.getName(), new BehaviourAdapter(behaviour, state)));
                }
            }

            tree.addAction("seq-" + sequenceName + "-end", _ -> {
                state.setCurrentStep(null);
                state.setSequenceEnded();
                return BehaviourTreeStatus.SUCCESS;
            });

            tree.end();
        }

        @Override
        public ShipBehaviourResult update(Ship ship) {
            if (tree == null) {
                state = new BehaviourTreeState(() -> ship);
                mapOnTree(treeBuilder, behaviours, state);
                tree = treeBuilder.build();
                logTree(tree);
            }

            BehaviourTreeStatus status = tickUntilNextBehaviour(ship);

            state.getCurrentStep()
                .ifPresent(currentStep -> {
                    logger.info("Running '{}' for Ship '{}'", currentStep.getName(), ship.getSymbol());
                });

            if (status == BehaviourTreeStatus.SUCCESS && state.isTreeDone()) {
                logger.debug("behaviour has no more steps left, returning done");
                return ShipBehaviourResult.done();
            }

            logger.debug("state: {}", status);
            logger.debug("callback: tree: {}, sequence: {}", state.getTreeState(), state.getSequenceState());

            if (state.isTreeDone()) {
                return ShipBehaviourResult.done();
            }

            return extractResult(() -> {
                    ShipBehaviourResult result = state.result;
                    if (result.isFailure()) {
                        logger.error("failed: {}", result);
                    }

                    if (state.isInSequence() && result.isFailure()) {
                        return ShipBehaviourResult.success();
                    }

                    return result;
                }
            );
        }

        private void logTree(BehaviourTreeNode tree) {
            if (!logger.isTraceEnabled()) {
                return;
            }

            logTree(tree, 0, false);
        }

        private void logTree(BehaviourTreeNode node, int depth, boolean isCurrent) {
            if (node instanceof ParentBehaviourTreeNode parent) {
                logger.trace("{}{}{}", getIndent(depth), isCurrent ? ">> " : "", node.getName());
                int childDepth = depth + 1;
                for (BehaviourTreeNode childNode : parent.getNodes()) {
                    logTree(childNode, childDepth, Objects.equals(parent.current(), childNode));
                }
            } else {
                logger.trace("{}{}{}", getIndent(depth), isCurrent ? ">> " : "", node.getName());
            }
        }

        private String getIndent(int depth) {
            return "  ".repeat(Math.max(0, depth));
        }

        private ShipBehaviourResult extractResult(Supplier<ShipBehaviourResult> supplier) {
            ShipBehaviourResult innerResult = supplier.get();
            logger.debug("innerResult before isDoneConversion: {}", innerResult);
            // 'Done' should only be sent by the final behaviour,
            // we interpreted it as a success so the parent behaviour can continue
            return innerResult.isDone() ? ShipBehaviourResult.success() : innerResult;
        }

        private final static class BehaviourTreeState {
            private final Logger logger = LoggerFactory.getLogger(BehaviourTreeState.class);

            private final Supplier<Ship> shipSupplier;

            private TreeState treeState = TreeState.READY;
            private SequenceState sequenceState = SequenceState.NONE;

            private ShipBehaviour currentStep;
            private ShipBehaviourResult result;
            private Float tick;

            private BehaviourTreeState(Supplier<Ship> shipSupplier) {
                this.shipSupplier = shipSupplier;
            }

            public Ship getShip() {
                return shipSupplier.get();
            }

            public void setCurrentStep(ShipBehaviour currentStep) {
                this.currentStep = currentStep;
                logger.debug("step: {}", currentStep);
            }

            public void setResult(ShipBehaviourResult result, Float tick) {
                this.result = result;
                this.tick = tick;
            }

            public void setTreeStarted() {
                treeState = TreeState.RUNNING;
            }

            public void setTreeEnded() {
                treeState = TreeState.DONE;
            }

            public void setSequenceStarted() {
                sequenceState = SequenceState.IN_SEQUENCE;
            }

            public void setSequenceEnded() {
                sequenceState = SequenceState.NONE;
            }

            public boolean isTreeDone() {
                return treeState == TreeState.DONE;
            }

            public boolean isInSequence() {
                return sequenceState == SequenceState.IN_SEQUENCE;
            }

            public Optional<ShipBehaviour> getCurrentStep() {
                return Optional.ofNullable(currentStep);
            }

            public TreeState getTreeState() {
                return treeState;
            }

            public SequenceState getSequenceState() {
                return sequenceState;
            }

            private enum TreeState {
                READY,
                RUNNING,
                DONE
            }

            private enum SequenceState {
                NONE,
                IN_SEQUENCE
            }
        }

        private final static class BehaviourAdapter implements Function<Float, BehaviourTreeStatus> {
            private final Logger logger = LoggerFactory.getLogger(BehaviourAdapter.class);

            private final ShipBehaviour leaf;
            private final BehaviourTreeState callback;

            BehaviourAdapter(ShipBehaviour leaf, BehaviourTreeState callback) {
                this.leaf = leaf;
                this.callback = callback;
            }

            @Override
            public BehaviourTreeStatus apply(Float tick) {
                callback.setCurrentStep(leaf);
                Objects.requireNonNull(tick, "tick shouldn't be null, something is probably broken");

                Ship currentShip = callback.getShip();
                logger.debug("running {} for ship '{}'", leaf.getName(), currentShip.getSymbol());
                ShipBehaviourResult result = leaf.update(currentShip);
                callback.setResult(result, tick);

                return transformResultToBehaviourTreeStatus(result);
            }

            private BehaviourTreeStatus transformResultToBehaviourTreeStatus(ShipBehaviourResult result) {
                if (result.isFailure()) {
                    return BehaviourTreeStatus.FAILURE;
                }

                return BehaviourTreeStatus.SUCCESS;
            }
        }
    }
}
