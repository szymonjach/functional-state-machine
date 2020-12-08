package yashku.fsm.machine;

import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.entries.StateEnterEntry;
import yashku.fsm.entries.StateTransitionEntry;
import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.state.PrimitiveState;

import java.util.List;

public class StateMachineBuilderWithDefinedEntryCallMethods<A extends PrimitiveState, T> {
    private final List<StateEnterEntry<A, PrimitiveAction<T>>> stateEnterEntries;

    public StateMachineBuilderWithDefinedEntryCallMethods(List<StateEnterEntry<A, PrimitiveAction<T>>> stateEnterEntries) {
        this.stateEnterEntries = stateEnterEntries;
    }

    public <B extends PrimitiveEvent> StateMachine<T> withDefinition(List<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries) {
        return StateMachine.withDefinition(entries, stateEnterEntries);
    }
}
