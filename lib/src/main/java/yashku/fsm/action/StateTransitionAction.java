package yashku.fsm.action;

import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.function.Function4;
import yashku.fsm.state.PrimitiveState;

public class StateTransitionAction<A extends PrimitiveState, B extends PrimitiveState, C extends PrimitiveEvent, T> extends Action4<A, B, C, T, T> {
    StateTransitionAction(Function4<A, B, C, T, T> function) {
        super(function);
    }
}
