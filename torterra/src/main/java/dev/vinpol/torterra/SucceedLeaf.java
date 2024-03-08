package dev.vinpol.torterra;

class SucceedLeaf<T> extends Leaf<T> {
    SucceedLeaf() {
        super();
    }

    @Override
	public void act(T instance) {
		succeed();
	}
}
