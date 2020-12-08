package yashku.fsm.machine;

import yashku.fsm.action.InternalStateTransitionAction;
import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.action.SimpleAction;
import yashku.fsm.action.StateTransitionAction;
import yashku.fsm.entries.StateTransitionEntry;
import yashku.fsm.entries.Transition;
import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.state.PrimitiveState;
import yashku.fsm.state.StartedState;

import java.util.Optional;
import java.util.stream.StreamSupport;

class StartedStateMachine<T, A extends PrimitiveState, B extends PrimitiveEvent> extends StateMachine<T> {
    private final Iterable<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries;

    public StartedStateMachine(Iterable<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries) {
        this.entries = entries;
    }

    @Override
    public T get() {
        return null;
    }

    @Override
    public PrimitiveState getState() {
        return entries.iterator().next().getFrom().getStartState();
    }

    @Override
    public StateMachine<T> newEvent(Transition transition) {
        final PrimitiveEvent event = transition.getEvent();
        return StreamSupport.stream(entries.spliterator(), false)
                .filter(entry -> entry.getEvent().is(event))
                .filter(entry -> entry.getFrom().is(transition.getFromState()))
                .map(this::makeTransitionBasedOnEntry)
                .findFirst()
                .orElse(this);
    }

    private StateMachine<T> makeTransitionBasedOnEntry(StateTransitionEntry<A, A, B, PrimitiveAction<T>> entry) {
        PrimitiveAction<T> action = entry.getAction();

        T transitionOutput = executeProperAction(action, entry);
        return entry.getTo().is(StartedState.Any) ? empty() : new TransitionedStateMachine<>(entry.getTo(), transitionOutput, Optional.of(this));
    }

    private T executeProperAction(PrimitiveAction<T> action, StateTransitionEntry<? extends PrimitiveState, ? extends PrimitiveState, ? extends PrimitiveEvent, ? extends PrimitiveAction<T>> entry) {
        if (action instanceof SimpleAction) {
            return action.run();
        } else if (action instanceof StateTransitionAction) {
            return ((StateTransitionAction<PrimitiveState, PrimitiveState, PrimitiveEvent, T>) action).run(entry.getFrom(), entry.getTo(), entry.getEvent(), get());
        } else if (action instanceof InternalStateTransitionAction) {
            return ((InternalStateTransitionAction<PrimitiveState, PrimitiveState, PrimitiveEvent, T>) action).run(entry.getFrom(), entry.getTo(), entry.getEvent(), this).get();
        }
        return action.run();
    }
}
