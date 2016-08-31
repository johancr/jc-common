package se.cronsioe.johan.base.func;

import java.util.Collection;

public class Chainer<T> {

    private final Collection<T> items;

    private Chainer(Collection<T> items) {
        this.items = items;
    }

    public static <T> Chainer<T> chain(Collection<T> items) {
        return new Chainer<T>(items);
    }

    public Chainer<T> filter(Predicate<T> predicate) {
        return chain(Filterer.filter(items).using(predicate));
    }

    public <R> Chainer<R> map(Function1<R, T> function) {
        return chain(Mapper.map(items).using(function));
    }

    public T reduce(T initialValue, Function2<T, T, T> function) {
        return Reducer.reduce(initialValue, items).using(function);
    }

    public <R> Chainer<R> flatMap(Function1<Collection<R>, T> function) {
        return chain(FlatMapper.flatMap(items).using(function));
    }
}
