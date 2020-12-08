package yashku.fsm.machine;

import yashku.fsm.entries.Transition;
import yashku.fsm.state.PrimitiveState;

public class StateMachineWithTransitionDeclineBehaviour<T> implements StateMachine<T> {
    private final Runnable sideEffect;
    private final StateMachine<T> stateMachine;

    public StateMachineWithTransitionDeclineBehaviour(StateMachine<T> stateMachine, Runnable sideEffect) {
        this.stateMachine = stateMachine;
        this.sideEffect = sideEffect;
    }

    @Override
    public T get() {
        return stateMachine.get();
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
            return this;
        }
        return new StateMachineWithTransitionDeclineBehaviour<>(result, sideEffect);
    }
}
