package se.cronsioe.johan.base.func;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReducerTest {

    @Test
    public void reduceEmpty() {
        Collection<Integer> empty = Collections.emptyList();

        int result = Reducer.reduce(0, empty).using(sumsAndAddsTen());

        assertThat(result, is(0));
    }

    private Function2<Integer, Integer, Integer> sumsAndAddsTen() {
        return new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer current, Integer value) {
                return current + value + 10;
            }
        };
    }

    @Test
    public void reduceOneFunction() {
        int result = Reducer.reduce(1, Arrays.asList(0)).using(sumsAndAddsTen());

        assertThat(result, is(11));
    }

    @Test
    public void reduceMultiple() {
        int result = Reducer.reduce(1, Arrays.asList(2, 3)).using(sumsAndAddsTen());

        assertThat(result, is(26));
    }

    @Test
    public void reduceString() {
        String result = Reducer.reduce("", Arrays.asList("hello ")).using(addsWorld());

        assertThat(result, is("hello world"));
    }

    private Function2<String, String, String> addsWorld() {
        return new Function2<String, String, String>() {
            @Override
            public String apply(String current, String value) {
                return value + "world";
            }
        };
    }
}
