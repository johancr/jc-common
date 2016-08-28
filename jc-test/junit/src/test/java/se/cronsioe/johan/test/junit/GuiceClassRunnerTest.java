package se.cronsioe.johan.test.junit;

import com.google.inject.AbstractModule;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GuiceClassRunnerTest {

    @Test
    public void injectsUsingImplementedBy() throws InitializationError {

        GuiceClassRunner guiceClassRunner = new GuiceClassRunner(TestsUsingImplementedBy.class);
        JUnitCore junitCore = new JUnitCore();

        Result result = junitCore.run(guiceClassRunner);

        assertThat(result.wasSuccessful(), is(true));
    }

    @Test
    public void injectUsingModulesInAnnotation() throws InitializationError {
        GuiceClassRunner guiceClassRunner = new GuiceClassRunner(TestsUsingAnnotation.class);
        JUnitCore junitCore = new JUnitCore();

        Result result = junitCore.run(guiceClassRunner);

        assertThat(result.wasSuccessful(), is(true));
    }

    @Test
    public void injectBeforeBefore() throws InitializationError {
        GuiceClassRunner guiceClassRunner = new GuiceClassRunner(TestsInjectBeforeBefore.class);
        JUnitCore junitCore = new JUnitCore();

        Result result = junitCore.run(guiceClassRunner);

        assertThat(result.wasSuccessful(), is(true));
    }

    @RunWith(GuiceClassRunner.class)
    public static class TestsUsingImplementedBy {

        @Inject
        private Foo foo;

        @Test
        public void fooReturnsFoo() {
            assertThat(foo.foo(), is("foo"));
        }

        @ImplementedBy(FooImpl.class)
        private interface Foo {
            String foo();
        }

        private static class FooImpl implements Foo {
            @Override
            public String foo() {
                return "foo";
            }
        }
    }

    @RunWith(GuiceClassRunner.class)
    @GuiceModules({TestsUsingAnnotation.Module.class})
    public static class TestsUsingAnnotation {
        @Inject
        private Foo foo;

        @Test
        public void fooReturnsFoo() {
            assertThat(foo.foo(), is("foo"));
        }

        private interface Foo {
            String foo();
        }

        private static class FooImpl implements Foo {
            @Override
            public String foo() {
                return "foo";
            }
        }

        public static class Module extends AbstractModule {
            @Override
            protected void configure() {
                bind(Foo.class).to(FooImpl.class);
            }
        }
    }

    @RunWith(GuiceClassRunner.class)
    public static class TestsInjectBeforeBefore {
        @Inject
        private Foo foo;

        @Before
        public void setUp() {
            assertThat(foo.foo(), is("foo"));
        }

        @Test
        public void justSoSetupRuns() {
        }

        @ImplementedBy(FooImpl.class)
        private interface Foo {
            String foo();
        }

        private static class FooImpl implements Foo {
            @Override
            public String foo() {
                return "foo";
            }
        }
    }
}
