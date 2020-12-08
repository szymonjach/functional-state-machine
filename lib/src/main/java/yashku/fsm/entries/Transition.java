package yashku.fsm.entries;

import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.state.PrimitiveState;

public class Transition {
    private final PrimitiveState fromState;
    private final PrimitiveEvent event;

    public Transition(PrimitiveState fromState, PrimitiveEvent event) {
        this.fromState = fromState;
        this.event = event;
    }


    public PrimitiveState getFromState() {
        return fromState;
    }

    public PrimitiveEvent getEvent() {
        return event;
    }
}
