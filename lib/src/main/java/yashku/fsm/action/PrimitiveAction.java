package yashku.fsm.action;

import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.function.Function3;
import yashku.fsm.function.Function4;
import yashku.fsm.machine.StateMachine;
import yashku.fsm.state.PrimitiveState;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PrimitiveAction<T> {

    T run();

    static <T> PrimitiveAction<T> of(Supplier<T> action) {
        return new SimpleAction<>(action);
    }

    static <T> PrimitiveAction<T> of(Runnable action) {
        return new SideEffectAction<>(action);
    }

    static <A, T> PrimitiveAction<T> of(Function<A, T> function) {
        return new Action1<>(function);
    }

    static <A, B, T> PrimitiveAction<T> of(BiFunction<A, B, T> function) {
        return new Action2<>(function);
    }

    static <A, B, C, T> PrimitiveAction<T> of(Function3<A, B, C, T> function) {
        return new Action3<>(function);
    }

    static <A, B, C, D, T> PrimitiveAction<T> of(Function4<A, B, C, D, T> function) {
        return new Action4<>(function);
    }

    static <A extends PrimitiveState, B extends PrimitiveState, C extends PrimitiveEvent, T> PrimitiveAction<T> ofTransition(Function4<A, B, C, T, T> function) {
        return new StateTransitionAction<>(function);
    }

    static <A extends PrimitiveState, B extends PrimitiveState, C extends PrimitiveEvent, T> PrimitiveAction<T> ofInternalTransition(Function4<A, B, C, StateMachine<T>, StateMachine<T>> function) {
        return new InternalStateTransitionAction<>(function);
    }

}
