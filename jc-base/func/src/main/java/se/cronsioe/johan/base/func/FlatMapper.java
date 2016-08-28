package se.cronsioe.johan.base.func;

import java.util.ArrayList;
import java.util.Collection;

public class FlatMapper<T> {

    private final Collection<T> items;

    public FlatMapper(Collection<T> items) {
        this.items = items;
    }

    public static <T> FlatMapper<T> flatMap(Collection<T> items) {
        return new FlatMapper<T>(items);
    }

    public <R> Collection<R> using(Function1<Collection<R>, T> function) {
        Collection<R> flattened = new ArrayList<R>();

        for (T item : items)
        {
            for (R mapped : function.apply(item))
            {
                flattened.add(mapped);
            }
        }
        return flattened;
    }
}
