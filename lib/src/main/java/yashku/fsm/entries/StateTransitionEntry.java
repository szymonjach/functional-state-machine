package yashku.fsm.entries;

import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.state.PrimitiveState;

import java.util.Objects;

public class StateTransitionEntry<F extends PrimitiveState, T extends PrimitiveState, E extends PrimitiveEvent, A extends PrimitiveAction<?>> {

    private final F from;
    private final T to;
    private final E event;
    private final A action;

    private StateTransitionEntry(F from, T to, E event, A action) {
        this.from = from;
        this.to = to;
        this.event = event;
        this.action = action;
    }

    public static <F extends PrimitiveState, T extends PrimitiveState, E extends PrimitiveEvent, A extends PrimitiveAction<?>>
    StateTransitionEntry<F, T, E, A> of(F from, T to, E event, A action) {
        Objects.requireNonNull(from, "From state has to be provided!");
        Objects.requireNonNull(to, "To state has to be provided!");
        Objects.requireNonNull(event, "Event has to be provided!");
        Objects.requireNonNull(action, "Action has to be provided!");
        return new StateTransitionEntry<>(from, to, event, action);
    }

    public F getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }

    public E getEvent() {
        return event;
    }

    public A getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateTransitionEntry<?, ?, ?, ?> that = (StateTransitionEntry<?, ?, ?, ?>) o;
        return getFrom().equals(that.getFrom()) &&
                getTo().equals(that.getTo()) &&
                getEvent().equals(that.getEvent()) &&
                getAction().equals(that.getAction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo(), getEvent(), getAction());
    }

    @Override
    public String toString() {
        return "StateTransitionEntry{" +
                "from=" + from +
                ", to=" + to +
                ", event=" + event +
                ", action=" + action +
                '}';
    }
}
