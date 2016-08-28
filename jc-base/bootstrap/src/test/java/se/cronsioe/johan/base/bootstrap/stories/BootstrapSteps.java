package se.cronsioe.johan.base.bootstrap.stories;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.cronsioe.johan.base.bootstrap.Bootstrap;
import se.cronsioe.johan.base.bootstrap.foo.FooService;
import se.cronsioe.johan.base.bootstrap.foo.FooServiceImpl;
import se.cronsioe.johan.base.bootstrap.impl.ModuleProvider;
import se.cronsioe.johan.base.bootstrap.impl.OverridingModuleMerger;
import se.cronsioe.johan.base.bootstrap.spi.ModuleMerger;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BootstrapSteps {

    private final Collection<Module> modules = new ArrayList<Module>();
    private final ModuleMerger moduleMerger = new OverridingModuleMerger();
    private Provider<Collection<Module>> moduleProvider;
    private Bootstrap bootstrap;
    private FooService fooService;

    @Given("^a ModuleProvider that provides a module with bindings for FooService$")
    public void a_ModuleProvider_that_provides_a_module_with_bindings_for_FooService() throws Throwable {
        modules.add(new FooModule());
        moduleProvider = new ModuleProvider() {
            @Override
            public Collection<Module> get() {
                return modules;
            }
        };
        bootstrap = new Bootstrap(moduleProvider, moduleMerger);
    }

    @When("^I get an instance of FooService$")
    public void i_get_an_instance_of_FooService() throws Throwable {
        fooService = bootstrap.getInstance(FooService.class);
    }

    @Then("^I get the implementation that was bound in the provided module$")
    public void i_get_the_implementation_that_was_bound_in_the_provided_module() throws Throwable {
        assertThat(fooService.foo(), is("foo"));
    }

    @When("^I add an additional module that overrides the bindings for FooService$")
    public void i_add_an_additional_module_that_overrides_the_bindings_for_FooService() throws Throwable {
        modules.add(new OverridingModule());
    }

    @Then("^I get the implementation that overrode the previous module$")
    public void i_get_the_implementation_that_overrode_the_previous_module() throws Throwable {
        fooService = bootstrap.getInstance(FooService.class);
        assertThat(fooService.foo(), is("overridden foo"));
    }

    private static final class FooModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(FooService.class).to(FooServiceImpl.class);
        }
    }

    private static final class OverridingModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(FooService.class).toInstance(new FooService() {
                @Override
                public String foo() {
                    return "overridden foo";
                }
            });
        }
    }
}
