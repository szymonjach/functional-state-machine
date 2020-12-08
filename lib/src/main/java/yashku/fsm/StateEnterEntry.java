package yashku.fsm;

public class StateEnterEntry<S extends PrimitiveState, A extends PrimitiveAction<?>> {

    private final PrimitiveAction<?> action;
    private final PrimitiveState state;

    public StateEnterEntry(S state, A action) {
        this.state = state;
        this.action = action;
    }

    public static <S extends PrimitiveState, A extends PrimitiveAction<?>> StateEnterEntry<S, A> of(S state, A action) {
        return new StateEnterEntry<>(state, action);
    }

    public PrimitiveState getState() {
        return state;
    }

    public PrimitiveAction<?> getAction() {
        return action;
    }

}
