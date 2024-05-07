package dev.vinpol.nebula.javaGOAP;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * IGoapUnit.java --- Interface which all assigned GoapAgents have to implement.
 *
 * @author P H - 12.03.2017
 */
public interface IGoapUnit {
    /**
     * Gets called when a plan was found by the planner.
     *
     * @param actions the actions the unit hat to take in order to archive the goal.
     */
    void goapPlanFound(Queue<GoapAction> actions);

    /**
     * Gets called when a plan failed to execute.
     *
     * @param actions the remaining actions in the action Queue that failed.
     */
    void goapPlanFailed(Queue<GoapAction> actions);

    /**
     * Gets called when a plan was finished.
     */
    void goapPlanFinished();

    /**
     * General update from the Agent. Called in a loop until the program ends.
     */
    void update();

    /**
     * Function to move to a specific location. Gets called by the moveToState
     * when the unit has to move to a certain target.
     *
     * @param target the target the unit has to move to.
     * @return true or false depending if the unit was able to move.
     */
    boolean moveTo(Object target);

    Set<GoapState> getWorldState();

    List<GoapState> getGoalState();

    Set<GoapAction> getAvailableActions();
}
