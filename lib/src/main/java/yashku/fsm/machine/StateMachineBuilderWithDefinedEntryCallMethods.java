package yashku.fsm.machine;

import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.entries.StateEnterEntry;
import yashku.fsm.entries.StateTransitionEntry;
import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.state.PrimitiveState;

import java.util.List;

public class StateMachineBuilderWithDefinedEntryCallMethods<A extends PrimitiveState> {
    private final List<StateEnterEntry<A>> stateEnterEntries;

    StateMachineBuilderWithDefinedEntryCallMethods(List<StateEnterEntry<A>> stateEnterEntries) {
        this.stateEnterEntries = stateEnterEntries;
    }

    public <B extends PrimitiveEvent, T> StateMachine<T> withDefinition(List<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries) {
        return StateMachine.withDefinition(entries, stateEnterEntries);
    }
}
