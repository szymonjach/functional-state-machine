package yashku.fsm.machine;

import yashku.fsm.entries.Transition;
import yashku.fsm.state.PrimitiveState;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class StateMachineWithSupressExceptionBehaviour<T> implements StateMachine<T> {
    private final StateMachine<T> stateMachine;
    private final Consumer<? super Exception> sideEffect;

    StateMachineWithSupressExceptionBehaviour(StateMachine<T> stateMachine, Consumer<? super Exception> sideEffect) {
        this.stateMachine = stateMachine;
        this.sideEffect = sideEffect;
    }

    @Override
    public T get() {
        return stateMachine.get();
    }

    @Override
    public StateMachine<T> map(Function<? super T, ? extends T> mapper) {
        Objects.requireNonNull(mapper, "(mapper) is null");
        return new StateMachineWithSupressExceptionBehaviour<>(stateMachine.map(mapper), sideEffect);
    }

    @Override
    public PrimitiveState getState() {
        return stateMachine.getState();
    }

    @Override
    public StateMachine<T> newEvent(Transition transition) {
        StateMachine<T> result;
        try {
            result = stateMachine.newEvent(transition);
        } catch (Exception e) {
            sideEffect.accept(e);
            return new StateMachineWithSupressExceptionBehaviour<>(stateMachine, sideEffect);
        }
        return result.equals(stateMachine) ? this : new StateMachineWithSupressExceptionBehaviour<>(result, sideEffect);
    }
}
