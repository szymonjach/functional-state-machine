package yashku.fsm.action;

import java.util.function.Function;

public class Action1<A, T> implements PrimitiveAction<T> {

    private final Function<A, T> function;

    Action1(Function<A, T> function) {
        this.function = function;
    }

    @Override
    public T run() {
        throw new IllegalStateException("Trying to run method which supposed not to be run!");
    }

    public T run(A arg) {
        return function.apply(arg);
    }
}
