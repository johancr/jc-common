package se.cronsioe.johan.base.bootstrap;

import com.google.inject.Module;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import se.cronsioe.johan.base.bootstrap.bar.BarModule;
import se.cronsioe.johan.base.bootstrap.bar.BarService;
import se.cronsioe.johan.base.bootstrap.foo.FooModule;
import se.cronsioe.johan.base.bootstrap.foo.FooService;
import se.cronsioe.johan.base.bootstrap.impl.ModuleProvider;
import se.cronsioe.johan.base.bootstrap.impl.OverridingModuleMerger;
import se.cronsioe.johan.base.bootstrap.missing.MissingBindingsModule;
import se.cronsioe.johan.test.lang.MetaInfServicesResourceBuilder;
import se.cronsioe.johan.test.lang.MockClassLoader;
import se.cronsioe.johan.test.lang.MockClassLoaderExecutor;
import se.cronsioe.johan.test.lang.MockClassLoaderTask;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BootstrapTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private MockClassLoaderExecutor executor = new MockClassLoaderExecutor();

    @Test
    public void loadBindingsUsingServiceLoader() throws IOException {

        executor.execute(new MockClassLoaderTask() {
            @Override
            public void configure(MockClassLoader mockClassLoader) {
                addResource(MetaInfServicesResourceBuilder
                        .service(Module.class)
                        .addImplementation(FooModule.class)
                        .build());
            }

            @Override
            public void run() {
                FooService fooService = new Bootstrap(new ModuleProvider(), new OverridingModuleMerger()).getInstance(FooService.class);

                assertThat(fooService.foo(), is("foo"));
            }
        });
    }

    @Test
    public void loadMultipleModules() throws IOException {

        executor.execute(new MockClassLoaderTask() {
            @Override
            public void configure(MockClassLoader mockClassLoader) {
                addResource(MetaInfServicesResourceBuilder
                        .service(Module.class)
                        .addImplementation(FooModule.class)
                        .addImplementation(BarModule.class)
                        .build());
            }

            @Override
            public void run() {
                Bootstrap bootstrap = new Bootstrap(new ModuleProvider(), new OverridingModuleMerger());

                FooService fooService = bootstrap.getInstance(FooService.class);
                BarService barService = bootstrap.getInstance(BarService.class);

                assertThat(fooService.foo(), is("foo"));
                assertThat(barService.bar(), is("bar"));
            }
        });
    }

    @Test
    public void throwIfModuleDoesntExist() throws IOException {

        executor.execute(new MockClassLoaderTask() {
            @Override
            public void configure(MockClassLoader mockClassLoader) {
                addResource(MetaInfServicesResourceBuilder
                        .service(Module.class)
                        .addImplementation("nonExisting.Module")
                        .build());
            }

            @Override
            public void run() {

                thrown.expect(RuntimeException.class);
                thrown.expectMessage("Could not locate module");

                new Bootstrap(new ModuleProvider(), new OverridingModuleMerger()).getInstance(Object.class);
            }
        });
    }

    @Test
    public void throwIfBindingsAreMissing() throws IOException {

        executor.execute(new MockClassLoaderTask() {
            @Override
            public void configure(MockClassLoader mockClassLoader) {
                addResource(MetaInfServicesResourceBuilder
                        .service(Module.class)
                        .addImplementation(MissingBindingsModule.class)
                        .build());
            }

            @Override
            public void run() {

                thrown.expect(RuntimeException.class);
                thrown.expectMessage("Could not bind to implementation");

                new Bootstrap(new ModuleProvider(), new OverridingModuleMerger()).getInstance(Object.class);
            }
        });
    }

}
