package dev.vinpol.torterra;

public abstract class Leaf<T> {

    private final String name;
    protected LeafState state = LeafState.IDLE;

    protected Leaf() {
        this.name = null;
    }

    protected Leaf(String name) {
        this.name = name;
    }

    public static <T> Leaf<T> newLeaf() {
        return new Leaf<>() {
            @Override
            public void act(T instance) {

            }
        };
    }

    public abstract void act(T instance);

    protected void succeed() {
        this.state = LeafState.SUCCESS;
    }

    protected void fail() {
        this.state = LeafState.FAILURE;
    }

    public boolean isSuccess() {
        return state.equals(LeafState.SUCCESS);
    }

    public boolean isFailure() {
        return state.equals(LeafState.FAILURE);
    }

    public LeafState getState() {
        return state;
    }

    public boolean isRunning() {
        return state.equals(LeafState.IDLE);
    }

    public enum LeafState {
        SUCCESS,
        FAILURE,
        IDLE
    }

    @Override
    public String toString() {
        if (name != null) {
            return name;
        }

        return "Leaf{" +
               "name='" + name + '\'' +
               ", state=" + state +
               '}';
    }
}
