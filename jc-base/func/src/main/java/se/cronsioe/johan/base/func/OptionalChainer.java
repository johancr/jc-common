package se.cronsioe.johan.base.func;

import java.util.Collection;

public class OptionalChainer<T> {

    private final Collection<T> items;

    private OptionalChainer(Collection<T> items) {
        this.items = items;
    }

    public static <T> OptionalChainer<T> chain(Collection<Optional<T>> optionals) {
        return new OptionalChainer<T>(Chainer.chain(optionals)
                .filter(new Predicate<Optional<T>>() {
                    @Override
                    public boolean test(Optional<T> value) {
                        return !value.isEmpty();
                    }
                })
                .map(new Function1<T, Optional<T>>() {
                    @Override
                    public T apply(Optional<T> value) {
                        return value.get();
                    }
                }).toCollection());
    }

    public OptionalChainer<T> filter(Predicate<T> predicate) {
        return new OptionalChainer<T>(Filterer.filter(items).using(predicate));
    }

    public <R> OptionalChainer<R> map(Function1<Optional<R>, T> function) {
        return chain(Mapper.map(items).using(function));
    }

    public T reduce(T initialValue, Function2<T, T, T> function) {
        return Reducer.reduce(initialValue, items).using(function);
    }

    public <R> OptionalChainer<R> flatMap(Function1<Collection<Optional<R>>, T> function) {
        return chain(FlatMapper.flatMap(items).using(function));
    }

    public Collection<T> toCollection() {
        return items;
    }
}
