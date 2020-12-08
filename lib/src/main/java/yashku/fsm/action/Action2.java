package yashku.fsm.action;

import java.util.function.BiFunction;

public class Action2<A, B, T> implements PrimitiveAction<T> {

    private final BiFunction<A, B, T> function;

    Action2(BiFunction<A, B, T> function) {
        this.function = function;
    }

    @Override
    public T run() {
        throw new IllegalStateException("Trying to run method which supposed not to be run!");
    }

    public T run(A argA, B argB) {
        return function.apply(argA, argB);
    }
}
