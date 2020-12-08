package yashku.fsm.action;

import yashku.fsm.function.Function4;

public class Action4<A, B, C, D, T> implements PrimitiveAction<T> {

    private final Function4<A, B, C, D, T> function;

    Action4(Function4<A, B, C, D, T> function) {
        this.function = function;
    }

    @Override
    public T run() {
        throw new IllegalStateException("Trying to run method which supposed not to be run!");
    }

    public T run(A argA, B argB, C argC, D argD) {
        return function.apply(argA, argB, argC, argD);
    }
}
