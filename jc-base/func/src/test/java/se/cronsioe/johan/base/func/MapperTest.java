package se.cronsioe.johan.base.func;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MapperTest {

    @Test
    public void mapEmpty() {
        Collection<?> result = Mapper.map(Collections.<Integer>emptyList()).using(null);

        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void mapInteger() {
        Collection<Integer> result = Mapper.map(Collections.singletonList(1)).using(addsTen());

        assertThat(result.iterator().next(), is(11));
    }

    private Function1<Integer, Integer> addsTen() {
        return new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer value) {
                return value + 10;
            }
        };
    }

    @Test
    public void mapMultiple() {
        Collection<Integer> result = Mapper.map(Arrays.asList(1, 2, 3)).using(addsTen());

        Iterator<Integer> iterator = result.iterator();
        assertThat(iterator.next(), is(11));
        assertThat(iterator.next(), is(12));
        assertThat(iterator.next(), is(13));
    }
}
