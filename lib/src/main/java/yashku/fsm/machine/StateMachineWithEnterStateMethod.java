package yashku.fsm.machine;

import yashku.fsm.entries.StateEnterEntry;
import yashku.fsm.entries.Transition;
import yashku.fsm.state.PrimitiveState;

import java.util.stream.StreamSupport;

class StateMachineWithEnterStateMethod<T, A extends PrimitiveState> implements StateMachine<T> {
    private final Iterable<StateEnterEntry<A>> stateEntryActions;
    private final StateMachine<T> stateMachine;

    StateMachineWithEnterStateMethod(StateMachine<T> stateMachine, Iterable<StateEnterEntry<A>> stateEnterEntries) {
        this.stateEntryActions = stateEnterEntries;
        this.stateMachine = stateMachine;
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
        StreamSupport.stream(stateEntryActions.spliterator(), false)
                .filter(entry -> entry.getState().is(result.getState()))
                .map(StateEnterEntry::getAction)
                .forEach(Runnable::run);
        if(result.equals(stateMachine)) {
            return this;
        }
        return new StateMachineWithEnterStateMethod<>(result, stateEntryActions);
    }
}
