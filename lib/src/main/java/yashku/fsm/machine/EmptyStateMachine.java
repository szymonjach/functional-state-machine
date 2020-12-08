package yashku.fsm.machine;

import yashku.fsm.entries.Transition;
import yashku.fsm.state.PrimitiveState;
import yashku.fsm.state.StartedState;

class EmptyStateMachine<T> extends StateMachine<T> {

    @Override
    public T get() {
        return null;
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
