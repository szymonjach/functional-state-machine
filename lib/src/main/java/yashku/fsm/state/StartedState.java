package yashku.fsm.state;

public enum StartedState implements PrimitiveState {
    Start,
    End,
    Unknown,
    Any;

    @Override
    public PrimitiveState getStartState() {
        return yashku.fsm.state.StartedState.Start;
    }
}
