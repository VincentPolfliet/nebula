package dev.vinpol.torterra;

import java.util.Objects;

class Selector<T> extends Leaf<T> {
	private final Iterable<Leaf<T>> leaves;

	public Selector(Iterable<Leaf<T>> leaves) {
        super();
        this.leaves = Objects.requireNonNull(leaves);
	}

	@Override
	public void act(T instance) {
		for (Leaf<T> leaf : leaves) {
			leaf.act(instance);

			if (leaf.isSuccess()) {
				succeed();
				return;
			}
		}

		fail();
	}
}
