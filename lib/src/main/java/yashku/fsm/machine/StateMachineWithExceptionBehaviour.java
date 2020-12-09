package yashku.fsm.machine;

import yashku.fsm.entries.Transition;
import yashku.fsm.state.PrimitiveState;
import yashku.fsm.state.StartedState;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class StateMachineWithExceptionBehaviour<T> implements StateMachine<T> {
    private final Consumer<? super Exception> sideEffect;
    private final StateMachine<T> stateMachine;
    private final Exception exception;

    StateMachineWithExceptionBehaviour(StateMachine<T> stateMachine, Consumer<? super Exception> sideEffect) {
        this(stateMachine, sideEffect, null);
    }

    private StateMachineWithExceptionBehaviour(StateMachine<T> stateMachine, Consumer<? super Exception> sideEffect, Exception e) {
        this.stateMachine = stateMachine;
        this.sideEffect = sideEffect;
        this.exception = e;
    }

    @Override
    public T get() {
        throw new StateMachineException(exception);
    }

    @Override
    public StateMachine<T> map(Function<? super T, ? extends T> mapper) {
        Objects.requireNonNull(mapper, "(mapper) is null");
        return new StateMachineWithExceptionBehaviour<>(stateMachine.map(mapper), sideEffect, exception);
    }

    @Override
    public PrimitiveState getState() {
        return StartedState.Unknown;
    }

    @Override
    public StateMachine<T> newEvent(Transition transition) {
        StateMachine<T> result;
        if(Objects.nonNull(exception)) {
            throw new StateMachineException(exception);
        }

        try {
            result = stateMachine.newEvent(transition);
        } catch (Exception e) {
            sideEffect.accept(e);
            return new StateMachineWithExceptionBehaviour<>(stateMachine, sideEffect, e);
        }
        return result.equals(stateMachine) ? this : new StateMachineWithExceptionBehaviour<>(result, sideEffect);
    }
}
