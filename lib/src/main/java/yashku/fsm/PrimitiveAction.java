package yashku.fsm;

import yashku.fsm.function.Function3;
import yashku.fsm.function.Function4;

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

    static <A extends PrimitiveState, B extends PrimitiveState, C extends PrimitiveEvent, T extends StateMachine<?>> PrimitiveAction<T> ofInternalTransition(Function4<A, B, C, T, T> function) {
        return new InternalStateTransitionAction<>(function);
    }

    class SimpleAction<T> implements PrimitiveAction<T> {

        private final Supplier<T> action;

        private SimpleAction(Supplier<T> action) {
            this.action = action;
        }

        @Override
        public T run() {
            return action.get();
        }
    }

    class SideEffectAction<Void> implements PrimitiveAction<Void> {
        private final Runnable action;

        private SideEffectAction(Runnable action) {
            this.action = action;
        }

        @Override
        public Void run() {
            action.run();
            return null;
        }
    }

    class Action1<A, T> implements PrimitiveAction<T> {

        private final Function<A, T> function;

        private Action1(Function<A, T> function) {
            this.function = function;
        }

        @Override
        public T run() {
            throw new IllegalStateException("Trying to run method which supposed not to be run!");
        }

        public T run(A arg) {
            return function.apply(arg);
        }
    }

    class Action2<A, B, T> implements PrimitiveAction<T> {

        private final BiFunction<A, B, T> function;

        private Action2(BiFunction<A, B, T> function) {
            this.function = function;
        }

        @Override
        public T run() {
            throw new IllegalStateException("Trying to run method which supposed not to be run!");
        }

        public T run(A argA, B argB) {
            return function.apply(argA, argB);
        }
    }

    class Action3<A, B, C, T> implements PrimitiveAction<T> {

        private final Function3<A, B, C, T> function;

        private Action3(Function3<A, B, C, T> function) {
            this.function = function;
        }

        @Override
        public T run() {
            throw new IllegalStateException("Trying to run method which supposed not to be run!");
        }

        public T run(A argA, B argB, C argC) {
            return function.apply(argA, argB, argC);
        }
    }

    class Action4<A, B, C, D, T> implements PrimitiveAction<T> {

        private final Function4<A, B, C, D, T> function;

        private Action4(Function4<A, B, C, D, T> function) {
            this.function = function;
        }

        @Override
        public T run() {
            throw new IllegalStateException("Trying to run method which supposed not to be run!");
        }

        public T run(A argA, B argB, C argC, D argD) {
            return function.apply(argA, argB, argC, argD);
        }
    }

    class StateTransitionAction<A extends PrimitiveState, B extends PrimitiveState, C extends PrimitiveEvent, T> extends Action4<A, B, C, T, T> {
        private StateTransitionAction(Function4<A, B, C, T, T> function) {
            super(function);
        }
    }

    class InternalStateTransitionAction<A extends PrimitiveState, B extends PrimitiveState, C extends PrimitiveEvent, T extends StateMachine<?>> extends Action4<A, B, C, T, T> {
        private InternalStateTransitionAction(Function4<A, B, C, T, T> function) {
            super(function);
        }
    }
}
