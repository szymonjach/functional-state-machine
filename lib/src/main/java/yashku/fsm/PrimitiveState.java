package yashku.fsm;

import java.io.Serializable;

public interface PrimitiveState extends Serializable {

    enum StartedState implements PrimitiveState {
        Start,
        End,
        Unknown,
        Any;

        @Override
        public PrimitiveState getStartState() {
            return StartedState.Start;
        }
    }

    default boolean is(PrimitiveState other) {
        return equals(other);
    }

    PrimitiveState getStartState();
}
