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
        Collection<Function1<Integer, Integer>> empty = Collections.emptyList();

        int result = Reducer.from(0).using(empty);

        assertThat(result, is(0));
    }

    @Test
    public void reduceOneFunction() {
        int result = Reducer.from(1).using(Arrays.asList(addsTen()));

        assertThat(result, is(11));
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
    public void reduceMultiple() {
        Collection<Function1<Integer, Integer>> functions = Arrays.asList(addsTen(), addsTen());

        int result = Reducer.from(1).using(functions);

        assertThat(result, is(21));
    }

    @Test
    public void reduceString() {
        String result = Reducer.from("hello ").using(Arrays.asList(addsWorld()));

        assertThat(result, is("hello world"));
    }

    private Function1<String, String> addsWorld() {
        return new Function1<String, String>() {
            @Override
            public String apply(String value) {
                return value + "world";
            }
        };
    }
}
