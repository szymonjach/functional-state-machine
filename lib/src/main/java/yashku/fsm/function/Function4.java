package yashku.fsm.function;

@FunctionalInterface
public interface Function4<A, B, C, D, R> {
    R apply(A var1, B var2, C var3, D var4);
}
