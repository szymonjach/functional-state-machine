package yashku.fsm;

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
