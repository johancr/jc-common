package se.cronsioe.johan.base.func;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FiltererTest {

    @Test
    public void filterNothing() {
        Collection<Object> result = Filterer.filter(Collections.emptyList()).using(null);

        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void filterOneOutOfTwo() {
        Collection<Integer> result = Filterer.filter(Arrays.asList(1, 2)).using(even());

        assertThat(result.iterator().next(), is(2));
    }

    private Predicate<Integer> even() {
        return new Predicate<Integer>() {
            @Override
            public boolean test(Integer value) {
                return value % 2 == 0;
            }
        };
    }
}
