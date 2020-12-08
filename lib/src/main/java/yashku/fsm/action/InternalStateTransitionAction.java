package yashku.fsm.action;

import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.function.Function4;
import yashku.fsm.machine.StateMachine;
import yashku.fsm.state.PrimitiveState;

public class InternalStateTransitionAction<A extends PrimitiveState, B extends PrimitiveState, C extends PrimitiveEvent, T> implements PrimitiveAction<T> {
    private final Function4<A, B, C, StateMachine<T>, StateMachine<T>> function;

    InternalStateTransitionAction(Function4<A, B, C, StateMachine<T>, StateMachine<T>> function) {
        this.function = function;
    }

    @Override
    public T run() {
        throw new IllegalStateException("Trying to run method which supposed not to be run!");
    }

    public StateMachine<T> run(A argA, B argB, C argC, StateMachine<T> argD) {
        return function.apply(argA, argB, argC, argD);
    }
}
