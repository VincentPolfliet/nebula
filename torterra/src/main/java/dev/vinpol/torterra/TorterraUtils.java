package dev.vinpol.torterra;

public final class TorterraUtils {

    private TorterraUtils() {

    }

    public static <T> Leaf<T> unwrap(Leaf<T> leaf) {
        if (leaf instanceof FailSafeLeaf<T> failSafeLeaf) {
            return failSafeLeaf.getInner();
        }

        return leaf;
    }
}
