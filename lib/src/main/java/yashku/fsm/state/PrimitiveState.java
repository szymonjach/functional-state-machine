package yashku.fsm.state;

import java.io.Serializable;

public interface PrimitiveState extends Serializable {

    default boolean is(PrimitiveState other) {
        return equals(other);
    }

    PrimitiveState getStartState();
}
