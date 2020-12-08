package yashku.fsm.action;

import yashku.fsm.function.Function3;

public class Action3<A, B, C, T> implements PrimitiveAction<T> {

    private final Function3<A, B, C, T> function;

    Action3(Function3<A, B, C, T> function) {
        this.function = function;
    }

    @Override
    public T run() {
        throw new IllegalStateException("Trying to run method which supposed not to be run!");
    }

    public T run(A argA, B argB, C argC) {
        return function.apply(argA, argB, argC);
    }
}
