package dev.vinpol.torterra;

/**
 * The {@code Tortilla} class provides utility methods for working with {@code Leaf} objects.
 * <p>
 * This class is called {@code Tortilla} because the method provided allows you to "unwrap" a {@code Leaf} object, much like you would unwrap a tortilla.
 * </p>
 */
public final class Tortilla {

    private Tortilla() {

    }

    /**
     * Unwraps the given {@code Leaf} object.
     * <p>
     * If the {@code Leaf} is an instance of {@code FailSafeLeaf}, this method returns the inner {@code Leaf}.
     * Otherwise, it returns the given {@code Leaf} as is.
     * </p>
     *
     * @param leaf the {@code Leaf} object to unwrap
     * @param <T>  the type of the value contained in the {@code Leaf}
     * @return the inner {@code Leaf} if the given {@code Leaf} is a {@code FailSafeLeaf}, otherwise the given {@code Leaf}
     */
    public static <T> Leaf<T> unwrap(Leaf<T> leaf) {
        if (leaf instanceof FailSafeLeaf<T> failSafeLeaf) {
            return failSafeLeaf.getInner();
        }

        return leaf;
    }
}
