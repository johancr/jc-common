package se.cronsioe.johan.base.func;

import java.util.Collection;

public class Reducer<T> {

    private final T initialValue;
    private Collection<T> items;

    private Reducer(T initialValue, Collection<T> items) {
        this.initialValue = initialValue;
        this.items = items;
    }

    public static <R> Reducer<R> reduce(R initialValue, Collection<R> items) {
        return new Reducer<R>(initialValue, items);
    }

    public T using(Function2<T, T, T> function) {
        T current = initialValue;
        for (T item : items)
        {
            current = function.apply(current, item);
        }
        return current;
    }
}
