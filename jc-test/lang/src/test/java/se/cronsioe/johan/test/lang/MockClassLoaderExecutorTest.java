package se.cronsioe.johan.test.lang;

import org.junit.Before;
import org.junit.Test;

import java.util.ServiceLoader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MockClassLoaderExecutorTest {

    private MockClassLoaderExecutor executor;

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
