package yashku.fsm;

import org.junit.jupiter.api.Test;
import yashku.fsm.action.PrimitiveAction;
import yashku.fsm.entries.StateEnterEntry;
import yashku.fsm.entries.StateTransitionEntry;
import yashku.fsm.event.PrimitiveEvent;
import yashku.fsm.machine.StateMachine;
import yashku.fsm.state.PrimitiveState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static yashku.fsm.StateMachineTest.CustomEvent.EventA;
import static yashku.fsm.StateMachineTest.CustomEvent.Start;
import static yashku.fsm.StateMachineTest.CustomState.S1;
import static yashku.fsm.StateMachineTest.CustomState.S2;

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

    @Test
    void testSimpleScenarioWithCustomStatesAndEvents() {
        var transitions = List.of(
                StateTransitionEntry.of(CustomState.Start, S1, Start, PrimitiveAction.ofTransition(this::startEvent)),
                StateTransitionEntry.of(S1, S2, EventA, PrimitiveAction.ofTransition(this::fromS1ToS2OnEventA)),
                StateTransitionEntry.of(S2, S1, EventA, PrimitiveAction.ofTransition(this::fromS1ToS2OnEventA))
//                StateTransitionEntry.of(S1, S2, EventA, PrimitiveAction.ofInternalTransition(this::fromS1ToS2OnEventAWithInternalTransition))
        );

        var sideEffectsOnEntry = List.of(
                StateEnterEntry.of(S2, () -> System.out.println("--\nSide effect xD\n--"))
        );

        StateMachine<String> stateMachine = StateMachine
                .withSideEffectWhenEntersState(sideEffectsOnEntry)
                .withDefinition(transitions)
                .newEvent(Start)
                .newEvent(EventA)
                .newEvent(EventA);

        assertEquals("ala", stateMachine.get());
    }

    private String startEvent(CustomState from, CustomState to, CustomEvent event, String context) {
        System.out.println(from);
        System.out.println(to);
        System.out.println(event);
        System.out.println(context);
        System.out.println("\n---\n");
        return null;
    }

    private String fromS1ToS2OnEventA(CustomState from, CustomState to, CustomEvent event, String context) {
        System.out.println(from);
        System.out.println(to);
        System.out.println(event);
        System.out.println(context);
        System.out.println("\n---\n");
        return "ala";
    }

    private StateMachine<String> fromS1ToS2OnEventAWithInternalTransition(CustomState from, CustomState to, CustomEvent event, StateMachine<String> context) {
        System.out.println(from);
        System.out.println(to);
        System.out.println(event);
        System.out.println(context);
        System.out.println("\n---\n");
        return context.newEvent(EventA);
    }
}
