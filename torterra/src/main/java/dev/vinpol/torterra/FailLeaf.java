package dev.vinpol.torterra;

class FailLeaf<T> extends Leaf<T> {
    FailLeaf() {
        super();
    }

    @Override
	public void act(T instance) {
		fail();
	}
}
