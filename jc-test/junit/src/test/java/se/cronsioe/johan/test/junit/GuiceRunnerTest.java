package se.cronsioe.johan.test.junit;

import com.google.inject.AbstractModule;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.multibindings.Multibinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GuiceRunnerTest {

    @Test
    public void injectsUsingImplementedBy() throws InitializationError {

        GuiceRunner guiceRunner = new GuiceRunner(TestsUsingImplementedBy.class);
        JUnitCore junitCore = new JUnitCore();

        Result result = junitCore.run(guiceRunner);

        assertThat(result.wasSuccessful(), is(true));
    }

    @Test
    public void injectUsingModulesInAnnotation() throws InitializationError {
        GuiceRunner guiceRunner = new GuiceRunner(TestsUsingAnnotation.class);
        JUnitCore junitCore = new JUnitCore();

        Result result = junitCore.run(guiceRunner);

        assertThat(result.wasSuccessful(), is(true));
    }

    @Test
    public void injectBeforeBefore() throws InitializationError {
        GuiceRunner guiceRunner = new GuiceRunner(TestsInjectBeforeBefore.class);
        JUnitCore junitCore = new JUnitCore();

        Result result = junitCore.run(guiceRunner);

        assertThat(result.wasSuccessful(), is(true));
    }

    @Test
    public void beforeAndAfterTasksAreRunBeforeAndAfter() throws InitializationError {
        GuiceRunner guiceRunner = new GuiceRunner(TestsBeforeAndAfterTask.class);
        JUnitCore junitCore = new JUnitCore();

        Result result = junitCore.run(guiceRunner);

        assertThat(result.wasSuccessful(), is(true));
    }

    @RunWith(GuiceRunner.class)
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

    @RunWith(GuiceRunner.class)
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

    @RunWith(GuiceRunner.class)
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

    @RunWith(GuiceRunner.class)
    @GuiceModules(TestsBeforeAndAfterTask.Module.class)
    public static class TestsBeforeAndAfterTask {

        private static String testValue = "not set";

        @Before
        public void setUp() {
            assertThat(testValue, is("not set"));
        }

        @After
        public void tearDown() {
            assertThat(testValue, is("set by after"));
        }

        @Test
        public void beforeTaskIsRun() {
            assertThat(testValue, is("set by before"));
        }

        public static class Module extends AbstractModule {
            @Override
            protected void configure() {
                Multibinder<BeforeAndAfterTask> tasks = Multibinder.newSetBinder(binder(), BeforeAndAfterTask.class);
                tasks.addBinding().toInstance(new BeforeAndAfterTask() {
                    @Override
                    public void before() {
                        testValue = "set by before";
                    }

                    @Override
                    public void after() {
                        testValue = "set by after";
                    }
                });
            }
        }
    }
}
