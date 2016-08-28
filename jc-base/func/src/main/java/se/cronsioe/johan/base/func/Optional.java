package se.cronsioe.johan.base.func;

import java.util.NoSuchElementException;

public class Optional<T> {

    private final T value;

    private Optional(T value) {
        this.value = value;
    }

    public static <T> Optional<T> empty() {
        return new Optional<T>(null);
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    public T get() {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }
}
