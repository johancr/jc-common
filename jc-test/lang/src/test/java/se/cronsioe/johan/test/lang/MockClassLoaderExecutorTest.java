package se.cronsioe.johan.test.lang;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ServiceLoader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MockClassLoaderExecutorTest {

    private MockClassLoaderExecutor executor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        executor = new MockClassLoaderExecutor();
    }

    @Test
    public void serviceLoader() {
        execute(new MockClassLoaderTask() {
            @Override
            public void configure(MockClassLoader mockClassLoader) {
                addResource(MetaInfServicesResourceBuilder.service(Foo.class)
                        .addImplementation(FooImpl.class).build());
            }

            @Override
            public void run() {
                ServiceLoader<Foo> serviceLoader = ServiceLoader.load(Foo.class);
                assertThat(serviceLoader.iterator().next().bar(), is("bar"));
            }
        });
    }

    @Test
    public void exceptionsArePropagated()
    {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("exception from task");

        execute(new MockClassLoaderTask() {
            @Override
            public void configure(MockClassLoader mockClassLoader) {

            }

            @Override
            public void run() {
                throw new RuntimeException("exception from task");
            }
        });
    }

    private void execute(MockClassLoaderTask task) {
        executor.execute(task);
    }

    public interface Foo {
        String bar();
    }

    public static class FooImpl implements Foo {
        @Override
        public String bar() {
            return "bar";
        }
    }

}
