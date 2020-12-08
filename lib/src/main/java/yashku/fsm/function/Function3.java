package yashku.fsm.function;

@FunctionalInterface
public interface Function3<A, B, C, R> {
    R apply(A var1, B var2, C var3);
}
