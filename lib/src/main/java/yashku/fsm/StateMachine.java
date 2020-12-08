package yashku.fsm;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public abstract class StateMachine<T> {

//    public static <S extends PrimitiveState, A extends PrimitiveAction<T>, T> StateMachine<T> withSideEffectWhenEntersState(List<StateEnterEntry<S, A>> entries) {
//        return
//    }

    public static <A extends PrimitiveState, B extends PrimitiveEvent, T> StateMachine<T> withDefinition(List<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries) {
        return new StartedStateMachine<>(entries);
    }

    private static <A extends PrimitiveState, B extends PrimitiveEvent, T> StateMachine<T> withDefinition(List<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries, List<StateEnterEntry<A, PrimitiveAction<T>>> stateEnterEntries) {
        return new StateMachineWithEnterStateMethod<>(withDefinition(entries), stateEnterEntries);
    }

    public static <T> StateMachine<T> empty() {
        return new EmptyStateMachine<>();
    }

    abstract public T get();

    abstract public PrimitiveState getState();

    public StateMachine<T> newEvent(final PrimitiveEvent event) {
        return newEvent(new Transition(getState(), event));
    }

    abstract public StateMachine<T> newEvent(final Transition transition);

    public static class StateMachineBuilderWithDefinedEntryCallMethods<A extends PrimitiveState, T> {
        private final List<StateEnterEntry<A, PrimitiveAction<T>>> stateEnterEntries;

        public StateMachineBuilderWithDefinedEntryCallMethods(List<StateEnterEntry<A, PrimitiveAction<T>>> stateEnterEntries) {
            this.stateEnterEntries = stateEnterEntries;
        }

        public <B extends PrimitiveEvent> StateMachine<T> withDefinition(List<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries) {
            return StateMachine.withDefinition(entries, stateEnterEntries);
        }
    }

    private static class EmptyStateMachine<T> extends StateMachine<T> {

        @Override
        public T get() {
            return null;
        }

        @Override
        public PrimitiveState getState() {
            return PrimitiveState.StartedState.Unknown;
        }

        @Override
        public StateMachine<T> newEvent(Transition transition) {
            return null;
        }
    }

    private static class StartedStateMachine<T, A extends PrimitiveState, B extends PrimitiveEvent> extends StateMachine<T> {
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
            return entry.getTo().is(PrimitiveState.StartedState.Any) ? empty() : new TransitionedStateMachine<>(entry.getTo(), transitionOutput, Optional.of(this));
        }

        private T executeProperAction(PrimitiveAction<T> action, StateTransitionEntry<? extends PrimitiveState, ? extends PrimitiveState, ? extends PrimitiveEvent, ? extends PrimitiveAction<T>> entry) {
            if (action instanceof PrimitiveAction.SimpleAction) {
                return action.run();
            } else if (action instanceof PrimitiveAction.StateTransitionAction) {
                return ((PrimitiveAction.StateTransitionAction<PrimitiveState, PrimitiveState, PrimitiveEvent, T>) action).run(entry.getFrom(), entry.getTo(), entry.getEvent(), get());
            }
            return action.run();
        }
    }

    private static class TransitionedStateMachine<T> extends StateMachine<T> {

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

    private static class StateMachineWithEnterStateMethod<T, A extends PrimitiveState> extends StateMachine<T> {
        private final Iterable<StateEnterEntry<A, PrimitiveAction<T>>> stateEntryActions;
        private final StateMachine<T> stateMachine;

        public StateMachineWithEnterStateMethod(StateMachine<T> stateMachine, Iterable<StateEnterEntry<A, PrimitiveAction<T>>> stateEnterEntries) {
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
        public StateMachine<T> newEvent(PrimitiveEvent event) {
            var result = stateMachine.newEvent(event);
            StreamSupport.stream(stateEntryActions.spliterator(), false)
                    .filter(entry -> entry.getState().is(result.getState()))
                    .map(StateEnterEntry::getAction)
                    .forEach(PrimitiveAction::run);
            return new TransitionedStateMachine<>(result, this);
        }

        @Override
        public StateMachine<T> newEvent(Transition transition) {
            var result = stateMachine.newEvent(transition);
            StreamSupport.stream(stateEntryActions.spliterator(), false)
                    .filter(entry -> entry.getState().is(result.getState()))
                    .map(StateEnterEntry::getAction)
                    .forEach(PrimitiveAction::run);
            return new TransitionedStateMachine<>(result, this);
        }
    }
}
