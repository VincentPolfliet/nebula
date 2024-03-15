package dev.vinpol.torterra;

class FailLeaf<T> extends StatefulLeaf<T> {
    FailLeaf() {
        super();
    }

    @Override
    public void doAct(T instance) {
        fail();
    }

    @Override
    public String toString() {
        return "fail";
    }
}
