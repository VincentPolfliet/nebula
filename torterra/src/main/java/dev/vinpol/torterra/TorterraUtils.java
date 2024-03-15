package dev.vinpol.torterra;

public final class TorterraUtils {

    private TorterraUtils() {

    }

    public static <T> Leaf<T> unwrap(Leaf<T> leaf) {
        if (leaf instanceof FailSafeLeaf<T> failSafeLeaf){
            return failSafeLeaf.getInner();
        }

        return leaf;
    }

    public static <T> void transferState(LeafState state, StatefulLeaf<T> target) {
        switch (state) {
            case SUCCESS -> target.succeed();
            case FAILED -> target.fail();
            case RUNNING -> {
                // do nothing
            }
        }
    }

    public static <T> void transferState(StatefulLeaf<T> from, StatefulLeaf<T> target) {
        if (from.isSuccess()) {
            target.succeed();
        } else if (from.isFailure()) {
            target.fail();
        }
    }
}
