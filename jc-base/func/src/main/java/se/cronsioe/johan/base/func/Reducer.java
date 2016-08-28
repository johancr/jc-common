package se.cronsioe.johan.base.func;

import java.util.Collection;

public class Reducer<R> {

    private final R initialValue;

    private Reducer(R initialValue) {
        this.initialValue = initialValue;
    }

    public static <R> Reducer<R> from(R initialValue) {
        return new Reducer<R>(initialValue);
    }

    public R using(Collection<Function1<R, R>> functions) {
        R result = initialValue;
        for (Function1<R, R> function : functions)
        {
            result = function.apply(result);
        }
        return result;
    }
}
