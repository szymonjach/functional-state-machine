package yashku.fsm.entries;

import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.state.PrimitiveState;

public class StateEnterEntry<S extends PrimitiveState> {

    private final Runnable action;
    private final PrimitiveState state;

    public StateEnterEntry(S state, Runnable action) {
        this.state = state;
        this.action = action;
    }

    public static <S extends PrimitiveState> StateEnterEntry<S> of(S state, Runnable action) {
        return new StateEnterEntry<>(state, action);
    }

    public PrimitiveState getState() {
        return state;
    }

    public Runnable getAction() {
        return action;
    }

}
