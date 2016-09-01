package se.cronsioe.johan.base.func;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ChainerTest {

    @Test
    public void chainsFunctionalConstructs() {
        int result = Chainer.chain(Arrays.asList(1, 2, 3))
                .filter(greaterThanOne())
                .map(timesTwo())
                .flatMap(repeat())
                .reduce(0, sum());

        assertThat(result, is(20));
    }

    @Test
    public void goBackToCollection() {

        Collection<Integer> result = Chainer.chain(Arrays.asList(1, 2, 3))
                .filter(greaterThanOne())
                .toCollection();

        assertThat(result.size(), is(2));
        assertThat(result, hasItems(2, 3));
    }

    private Predicate<Integer> greaterThanOne() {
        return new Predicate<Integer>() {
            @Override
            public boolean test(Integer value) {
                return value > 1;
            }
        };
    }

    private Function1<Integer, Integer> timesTwo() {
        return new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer value) {
                return value * 2;
            }
        };
    }

    private Function1<Collection<Integer>, Integer> repeat() {
        return new Function1<Collection<Integer>, Integer>() {
            @Override
            public Collection<Integer> apply(Integer value) {
                return Arrays.asList(value, value);
            }
        };
    }

    private Function2<Integer, Integer, Integer> sum() {
        return new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer current, Integer value) {
                return current + value;
            }
        };
    }
}
