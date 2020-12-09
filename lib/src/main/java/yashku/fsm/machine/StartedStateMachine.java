package yashku.fsm.machine;

import yashku.fsm.action.InternalStateTransitionAction;
import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.action.StateTransitionAction;
import yashku.fsm.entries.StateTransitionEntry;
import yashku.fsm.entries.Transition;
import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.state.PrimitiveState;
import yashku.fsm.state.StartedState;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.StreamSupport;

class StartedStateMachine<T, A extends PrimitiveState, B extends PrimitiveEvent> implements StateMachine<T> {
    private final Iterable<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries;
    private final T value;
    private final PrimitiveState state;

    StartedStateMachine(Iterable<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries) {
        this(entries, entries.iterator().next().getFrom().getStartState(), null);
    }

    private StartedStateMachine(Iterable<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries, PrimitiveState state, T value) {
        this.entries = entries;
        this.value = value;
        this.state = state;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public StateMachine<T> map(Function<? super T, ? extends T> mapper) {
        Objects.requireNonNull(mapper, "(mapper) is null");
        return new StartedStateMachine<>(entries, getState(), mapper.apply(get()));
    }

    @Override
    public PrimitiveState getState() {
        return state;
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
        return entry.getTo().is(StartedState.Any) ? StateMachine.empty() : new StartedStateMachine<>(entries, entry.getTo(), transitionOutput);
    }

    private T executeProperAction(PrimitiveAction<T> action, StateTransitionEntry<? extends PrimitiveState, ? extends PrimitiveState, ? extends PrimitiveEvent, ? extends PrimitiveAction<T>> entry) {
        if (action instanceof StateTransitionAction) {
            return ((StateTransitionAction<PrimitiveState, PrimitiveState, PrimitiveEvent, T>) action).run(entry.getFrom(), entry.getTo(), entry.getEvent(), get());
        } else if (action instanceof InternalStateTransitionAction) {
            return ((InternalStateTransitionAction<PrimitiveState, PrimitiveState, PrimitiveEvent, T>) action).run(entry.getFrom(), entry.getTo(), entry.getEvent(), this).get();
        }
        return action.run();
    }
}
