package se.cronsioe.johan.base.func.stories;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.cronsioe.johan.base.func.*;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FuncSteps {

    private Collection<Integer> integers;
    private int result;

    @Given("^a collections of the integers (.*)$")
    public void a_collections_of_the_integers(List<Integer> integers) throws Throwable {
        this.integers = integers;
    }

    @When("^I map using a function that adds (\\d+)$")
    public void i_map_using_a_function_that_adds(final int added) throws Throwable {

        integers = Mapper.map(integers).using(new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer value) {
                return value + added;
            }
        });
    }

    @When("^I filter integers greater than (\\d+)$")
    public void i_filter_integers_greater_than(final int tooLow) throws Throwable {

        integers = Filterer.from(integers).using(new Predicate<Integer>() {
            @Override
            public boolean test(Integer value) {
                return value > tooLow;
            }
        });
    }

    @When("^I reduce$")
    public void i_reduce() throws Throwable {

        result = Reducer.from(0).using(Mapper.map(integers).using(new Function1<Function1<Integer, Integer>, Integer>() {
            @Override
            public Function1<Integer, Integer> apply(final Integer value) {
                return new Function1<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer sum) {
                        return sum + value;
                    }
                };
            }
        }));
    }

    @Then("^the result is (\\d+)$")
    public void the_result_is(int expected) throws Throwable {
        assertThat(result, is(expected));
    }
}
