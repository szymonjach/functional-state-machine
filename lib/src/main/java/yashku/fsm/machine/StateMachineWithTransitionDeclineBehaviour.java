package yashku.fsm.machine;

import yashku.fsm.entries.Transition;
import yashku.fsm.state.PrimitiveState;

import java.util.Objects;
import java.util.function.Function;

public class StateMachineWithTransitionDeclineBehaviour<T> implements StateMachine<T> {
    private final Runnable sideEffect;
    private final StateMachine<T> stateMachine;

    StateMachineWithTransitionDeclineBehaviour(StateMachine<T> stateMachine, Runnable sideEffect) {
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
        return new StateMachineWithTransitionDeclineBehaviour<>(stateMachine.map(mapper), sideEffect);
    }

    @Override
    public PrimitiveState getState() {
        return stateMachine.getState();
    }

    @Override
    public StateMachine<T> newEvent(Transition transition) {
        var result = stateMachine.newEvent(transition);
        if(result.equals(stateMachine)) {
            sideEffect.run();
            return new StateMachineWithTransitionDeclineBehaviour<>(stateMachine, sideEffect);
        }
        return result.equals(stateMachine) ? this : new StateMachineWithTransitionDeclineBehaviour<>(result, sideEffect);
    }
}
