package yashku.fsm;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.entries.StateEnterEntry;
import yashku.fsm.entries.StateTransitionEntry;
import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.function.Function4;
import yashku.fsm.machine.StateMachine;
import yashku.fsm.state.PrimitiveState;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StateMachineTest {

    enum CustomState implements PrimitiveState {
        Start,
        S1,
        S2,
        S3,
        S4;

        @Override
        public PrimitiveState getStartState() {
            return CustomState.Start;
        }
    }

    enum CustomEvent implements PrimitiveEvent {
        Start,
        EventA,
        EventB,
        EventC
    }

    private final AtomicReference<Function4<CustomState, CustomState, CustomEvent, String, String>> startEvent = new AtomicReference<>();
    private final AtomicReference<Function4<CustomState, CustomState, CustomEvent, String, String>> transitionAction = new AtomicReference<>();

    List<StateTransitionEntry<CustomState, CustomState, CustomEvent, PrimitiveAction<String>>> transitions = List.of(
            StateTransitionEntry.of(CustomState.Start, CustomState.S1, CustomEvent.Start, PrimitiveAction.<CustomState, CustomState, CustomEvent, String>ofTransition((s1, s2, e, s) -> startEvent.get().apply(s1, s2, e, s))),
            StateTransitionEntry.of(CustomState.S1, CustomState.S2, CustomEvent.EventA, PrimitiveAction.<CustomState, CustomState, CustomEvent, String>ofTransition((s1, s2, e, s) -> transitionAction.get().apply(s1, s2, e, s))),
            StateTransitionEntry.of(CustomState.S2, CustomState.S3, CustomEvent.EventB, PrimitiveAction.<CustomState, CustomState, CustomEvent, String>ofTransition((s1, s2, e, s) -> transitionAction.get().apply(s1, s2, e, s))),
            StateTransitionEntry.of(CustomState.S3, CustomState.S4, CustomEvent.EventC, PrimitiveAction.<CustomState, CustomState, CustomEvent, String>ofTransition((s1, s2, e, s) -> transitionAction.get().apply(s1, s2, e, s))),
            StateTransitionEntry.of(CustomState.S4, CustomState.S1, CustomEvent.EventC, PrimitiveAction.<CustomState, CustomState, CustomEvent, String>ofTransition((s1, s2, e, s) -> transitionAction.get().apply(s1, s2, e, s)))
    );

    @Test
    void simple_transition_is_working_fine() {
        startEvent.set((s1, s2, e, o) -> "");
        transitionAction.set((s1, s2, e, o) -> "");

        StateMachine<String> stateMachine = StateMachine.withDefinition(transitions)
                .newEvent(CustomEvent.Start);

        assertEquals(CustomState.S1, stateMachine.getState());
    }

    @Test
    void one_transition_is_able_to_produce_value_which_is_passed_to_the_second_transition() {
        startEvent.set((s1, s2, e, o) -> "something");
        transitionAction.set((s1, s2, e, o) -> o + " more");

        StateMachine<String> stateMachine = StateMachine.withDefinition(transitions)
                .newEvent(CustomEvent.Start)
                .newEvent(CustomEvent.EventA);

        assertEquals("something more", stateMachine.get());
    }

    @Test
    void state_machine_is_immutable() {
        startEvent.set((s1, s2, e, o) -> "something");
        transitionAction.set((s1, s2, e, o) -> o + " more");

        StateMachine<String> stateMachine = StateMachine.withDefinition(transitions);
        var stateMachineAfterStartEvent = stateMachine.newEvent(CustomEvent.Start);
        var stateMachineAfterStartEventAndEventA = stateMachineAfterStartEvent.newEvent(CustomEvent.EventA);

        assertEquals(CustomState.Start, stateMachine.getState());
        assertEquals(CustomState.S1, stateMachineAfterStartEvent.getState());
        assertEquals(CustomState.S2, stateMachineAfterStartEventAndEventA.getState());
    }

    @Test
    void arguments_in_transitions_are_as_expected() {
        AtomicReference<CustomState> fromState = new AtomicReference<>();
        AtomicReference<CustomState> toState = new AtomicReference<>();
        AtomicReference<CustomEvent> event = new AtomicReference<>();

        startEvent.set((s1, s2, e, o) -> {
            fromState.set(s1);
            toState.set(s2);
            event.set(e);
            return "empty";
        });
        transitionAction.set((s1, s2, e, o) -> o + " output");

        StateMachine.withDefinition(transitions).newEvent(CustomEvent.Start);

        assertEquals(CustomState.Start, fromState.get());
        assertEquals(CustomState.S1, toState.get());
        assertEquals(CustomEvent.Start, event.get());
    }

    @Test
    void side_effect_on_entry_state_is_executed() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        System.out.println("ala ma kota");
        startEvent.set((s1, s2, e, o) -> "empty");
        transitionAction.set((s1, s2, e, o) -> o + " output");

        var entryStateSideEffects = List.of(
                StateEnterEntry.of(CustomState.S1, () -> atomicBoolean.set(true))
        );

        StateMachine.withSideEffectWhenEntersState(entryStateSideEffects).withDefinition(transitions).newEvent(CustomEvent.Start);

        assertTrue(atomicBoolean.get(), "Side effect was not executed");
    }
}
