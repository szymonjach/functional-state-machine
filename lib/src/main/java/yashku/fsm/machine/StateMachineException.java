package yashku.fsm.machine;

public class StateMachineException extends RuntimeException {
    private final Exception exception;

    StateMachineException(Exception e) {
        this.exception = e;
    }

    public Exception getEmbeddedException() {
        return exception;
    }
}
