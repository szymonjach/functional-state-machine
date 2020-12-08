package yashku.fsm.action;

import java.util.function.Supplier;

public class SimpleAction<T> implements PrimitiveAction<T> {

    private final Supplier<T> action;

    SimpleAction(Supplier<T> action) {
        this.action = action;
    }

    @Override
    public T run() {
        return action.get();
    }
}
