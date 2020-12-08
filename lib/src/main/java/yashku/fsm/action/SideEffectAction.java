package yashku.fsm.action;

public class SideEffectAction<Void> implements PrimitiveAction<Void> {
    private final Runnable action;

    SideEffectAction(Runnable action) {
        this.action = action;
    }

    @Override
    public Void run() {
        action.run();
        return null;
    }
}
