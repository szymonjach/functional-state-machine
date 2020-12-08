package yashku.fsm.machine;

import yashku.fsm.entries.Transition;
import yashku.fsm.state.PrimitiveState;

import java.util.Optional;

class TransitionedStateMachine<T> implements StateMachine<T> {

    private final T transitionOutput;
    private final PrimitiveState state;
    private final Optional<StateMachine<T>> composedStateMachine;

    public TransitionedStateMachine(PrimitiveState state, T transitionOutput, Optional<StateMachine<T>> composedStateMachine) {
        this.state = state;
        this.transitionOutput = transitionOutput;
        this.composedStateMachine = composedStateMachine;
    }

    public TransitionedStateMachine(StateMachine<T> baseStateMachine, StateMachine<T> extendedStateMachine) {
        this(baseStateMachine.getState(), baseStateMachine.get(), Optional.of(extendedStateMachine));
    }

    @Override
    public T get() {
        return transitionOutput;
    }

    @Override
    public PrimitiveState getState() {
        return state;
    }

    @Override
    public StateMachine<T> newEvent(Transition transition) {
        return composedStateMachine.map(fsm -> fsm.newEvent(transition)).orElseThrow();
    }
}
