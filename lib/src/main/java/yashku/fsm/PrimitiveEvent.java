package yashku.fsm;

public interface PrimitiveEvent {
    default boolean is(PrimitiveEvent other) {
        return equals(other);
    }
}
