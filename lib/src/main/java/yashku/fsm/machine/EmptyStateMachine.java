package yashku.fsm.machine;

import yashku.fsm.entries.Transition;
import yashku.fsm.state.PrimitiveState;
import yashku.fsm.state.StartedState;

import java.util.Objects;
import java.util.function.Function;

class EmptyStateMachine<T> implements StateMachine<T> {

    EmptyStateMachine() {

    }

    @Override
    public T get() {
        return null;
    }

    @Override
    public StateMachine<T> map(Function<? super T, ? extends T> mapper) {
        Objects.requireNonNull(mapper, "(mapper) is null");
        return this;
    }

    @Override
    public PrimitiveState getState() {
        return StartedState.Unknown;
    }

    @Override
    public StateMachine<T> newEvent(Transition transition) {
        return null;
    }
}
