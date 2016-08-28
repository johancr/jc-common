package se.cronsioe.johan.base.func;

import java.util.ArrayList;
import java.util.Collection;

public class Filterer<T> {

    private final Collection<T> items;

    private Filterer(Collection<T> items) {
        this.items = items;
    }

    public static <T> Filterer<T> from(Collection<T> items) {
        return new Filterer<T>(items);
    }

    public Collection<T> using(Predicate<T> predicate) {
        Collection<T> passed = new ArrayList<T>();

        for (T item : items)
        {
            if (predicate.test(item))
            {
                passed.add(item);
            }
        }

        return passed;
    }
}
