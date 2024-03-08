package dev.vinpol.torterra;

public final class TorterraUtils {

    private TorterraUtils() {

    }

    public static <T> void transferState(Leaf<T> from, Leaf<T> target) {
        switch (from.getState()) {
            case SUCCESS -> {
                target.succeed();
            }
            case FAILURE -> {
                target.fail();
            }
            case IDLE -> {
                // do nothing
            }
        }
    }
}
