package se.cronsioe.johan.base.func;

import java.util.ArrayList;
import java.util.Collection;

public class Mapper<T> {

    private final Collection<T> items;

    private Mapper(Collection<T> items) {
        this.items = items;
    }

    public static <T> Mapper<T> map(Collection<T> items) {
        return new Mapper<T>(items);
    }

    public <R> Collection<R> using(final Function1<R, T> function) {

        Collection<R> mapped = new ArrayList<R>();

        for (final T item : items)
        {
            mapped.add(function.apply(item));
        }

        return mapped;
    }
}
