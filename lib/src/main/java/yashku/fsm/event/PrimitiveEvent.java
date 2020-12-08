package yashku.fsm.event;

public interface PrimitiveEvent {
    default boolean is(PrimitiveEvent other) {
        return equals(other);
    }
}
