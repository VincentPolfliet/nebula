package dev.vinpol.nebula.javaGOAP;

import java.util.Objects;

/**
 * GoapState.java --- States which the GoapActions use to build a graph
 *
 * @author P H - 28.01.2017
 */
public class GoapState {

    public Integer importance = 0;
    public String effect;
    public Object value;

    public GoapState(String effect, Object value) {
        this.effect = effect;
        this.value = value;
    }

    /**
     * @param importance the importance of the state being reached Only necessary if
     *                   the state is used to define a worldState. Has no effect in
     *                   Actions being taken. Do NOT set this to Integer.MaxValue since
     *                   this causes the goal to be removed from the HashSet by the
     *                   Planner.
     * @param effect     the effect the state has.
     * @param value      the value of the effect. Since "Object" is being used this is
     *                   NOT type safe!
     */
    public GoapState(Integer importance, String effect, Object value) {
        if (importance == null || importance < 0) {
            importance = 0;
        }

        this.importance = importance;
        this.effect = effect;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoapState goapState)) return false;
        return Objects.equals(importance, goapState.importance) && Objects.equals(effect, goapState.effect) && Objects.equals(value, goapState.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(importance, effect, value);
    }

    @Override
    public String toString() {
        return "GoapState{" +
               "importance=" + importance +
               ", effect='" + effect + '\'' +
               ", value=" + value +
               '}';
    }
}
