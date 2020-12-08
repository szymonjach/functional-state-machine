package yashku.fsm.machine;

import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.entries.StateEnterEntry;
import yashku.fsm.entries.StateTransitionEntry;
import yashku.fsm.entries.Transition;
import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.state.PrimitiveState;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface StateMachine<T> {

    static <S extends PrimitiveState> StateMachineBuilderWithDefinedEntryCallMethods<S> withSideEffectWhenEntersState(List<StateEnterEntry<S>> entries) {
        return new StateMachineBuilderWithDefinedEntryCallMethods<>(entries);
    }

    static <A extends PrimitiveState, B extends PrimitiveEvent, T> StateMachine<T> withDefinition(List<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries) {
        return new StartedStateMachine<>(entries);
    }

    static <A extends PrimitiveState, B extends PrimitiveEvent, T> StateMachine<T> withDefinition(List<StateTransitionEntry<A, A, B, PrimitiveAction<T>>> entries, List<StateEnterEntry<A>> stateEnterEntries) {
        return new StateMachineWithEnterStateMethod<>(withDefinition(entries), stateEnterEntries);
    }

    static <T> StateMachine<T> empty() {
        return new EmptyStateMachine<>();
    }

    T get();

    default <U> Optional<U> mapOptional(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "(mapper) is null");
        return toOptional().map(mapper);
    }

    default Optional<T> toOptional() {
        return Optional.of(get());
    }

    default <U> StateMachine<U> flatMap(Function<? super T, ? extends StateMachine<U>> mapper) {
        Objects.requireNonNull(mapper, "(mapper) is null");
        return mapper.apply(get());
    }

    PrimitiveState getState();

    default StateMachine<T> onTransitionDecline(Runnable sideEffect) {
        return new StateMachineWithTransitionDeclineBehaviour<>(this, sideEffect);
    }

    default StateMachine<T> onException(Consumer<? super Exception> sideEffect) {
        return new StateMachineWithExceptionBehaviour<>(this, sideEffect);
    }

    default StateMachine<T> onExceptionSuppress(Consumer<? super Exception> sideEffect) {
        return new StateMachineWithSupressExceptionBehaviour<>(this, sideEffect);
    }

//    default StateMachine<T> onTransitionDecline(PrimitiveAction<T> action) {
//        return new StateMachineWithTransitionDeclineBehaviour<>(this, action);
//    }

    default StateMachine<T> newEvent(final PrimitiveEvent event) {
        return newEvent(new Transition(getState(), event));
    }

    StateMachine<T> newEvent(final Transition transition);

}
