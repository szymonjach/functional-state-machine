package yashku.fsm.machine;

import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.entries.StateEnterEntry;
import yashku.fsm.entries.StateTransitionEntry;
import yashku.fsm.entries.Transition;
import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.state.PrimitiveState;

import java.util.List;
import java.util.Optional;

public interface StateMachine<T> {

//    public static <S extends PrimitiveState, A extends PrimitiveAction<T>, T> StateMachine<T> withSideEffectWhenEntersState(List<StateEnterEntry<S, A>> entries) {
//        return
//    }

    static <A extends PrimitiveState, B extends PrimitiveEvent, T> StateMachine<T> withDefinition(List<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries) {
        return new StartedStateMachine<>(entries);
    }

    static <A extends PrimitiveState, B extends PrimitiveEvent, T> StateMachine<T> withDefinition(List<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries, List<StateEnterEntry<A, PrimitiveAction<T>>> stateEnterEntries) {
        return new StateMachineWithEnterStateMethod<>(withDefinition(entries), stateEnterEntries);
    }

    static <T> StateMachine<T> empty() {
        return new EmptyStateMachine<>();
    }

    T get();

    PrimitiveState getState();

    default StateMachine<T> newEvent(final PrimitiveEvent event) {
        return newEvent(new Transition(getState(), event));
    }

    StateMachine<T> newEvent(final Transition transition);

}
