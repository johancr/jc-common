package se.cronsioe.johan.base.func;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OptionalChainerTest {

    @Test
    public void chainOfOptionalsIgnoresEmpty() {
        int result = OptionalChainer.chain(Arrays.asList(Optional.of(1), Optional.<Integer>empty(), Optional.of(2)))
                .map(timesTwo())
                .flatMap(appendEmpty())
                .flatMap(repeat())
                .filter(greaterThanTwo())
                .reduce(0, sum());

        assertThat(result, is(8));
    }

    private Function1<Optional<Integer>, Integer> timesTwo() {
        return new Function1<Optional<Integer>, Integer>() {
            @Override
            public Optional<Integer> apply(Integer value) {
                if (value % 2 == 0)
                {
                    return Optional.of(value * 2);
                }
                else
                {
                    return Optional.empty();
                }
            }
        };
    }

    private Predicate<Integer> greaterThanTwo() {
        return new Predicate<Integer>() {
            @Override
            public boolean test(Integer value) {
                return value > 2;
            }
        };
    }

    private Function1<Collection<Optional<Integer>>, Integer> repeat() {
        return new Function1<Collection<Optional<Integer>>, Integer>() {
            @Override
            public Collection<Optional<Integer>> apply(Integer value) {
                return Arrays.asList(Optional.of(value), Optional.of(value));
            }
        };
    }

    private Function1<Collection<Optional<Integer>>, Integer> appendEmpty() {
        return new Function1<Collection<Optional<Integer>>, Integer>() {
            @Override
            public Collection<Optional<Integer>> apply(Integer value) {
                return Arrays.asList(Optional.of(value), Optional.<Integer>empty());
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
