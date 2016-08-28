package se.cronsioe.johan.base.func;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OptionalTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void empty() {
        Optional<Integer> optional = Optional.empty();

        assertThat(optional.isEmpty(), is(true));
    }

    @Test
    public void notEmpty() {
        Optional<Integer> optional = Optional.of(2);

        assertThat(optional.isEmpty(), is(false));
        assertThat(optional.get(), is(2));
    }

    @Test
    public void throwIfCallingGetWhenEmpty() {
        Optional<Integer> optional = Optional.empty();

        thrown.expect(NoSuchElementException.class);

        optional.get();
    }
}
