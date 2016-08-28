package se.cronsioe.johan.base.bootstrap.stories;

import com.google.inject.Module;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.cronsioe.johan.base.bootstrap.foo.FooModule;
import se.cronsioe.johan.base.bootstrap.impl.ModuleProvider;
import se.cronsioe.johan.test.lang.MetaInfServicesResourceBuilder;
import se.cronsioe.johan.test.lang.MockClassLoader;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ServiceLoaderBasedProviderSteps {

    private ModuleProvider moduleProvider = new ModuleProvider();
    private Collection<Module> modules;
    private MockClassLoader mockClassLoader;

    private ClassLoader realContextClassLoader;

    @Before
    public void setUp() {
        realContextClassLoader = Thread.currentThread().getContextClassLoader();

        mockClassLoader = new MockClassLoader();
        Thread.currentThread().setContextClassLoader(mockClassLoader);
    }

    @After
    public void tearDown() {
        Thread.currentThread().setContextClassLoader(realContextClassLoader);
    }

    @Given("^a file named com\\.google\\.inject\\.Module in META-INF/services$")
    public void a_file_named_com_google_inject_Module_in_META_INF_services() throws Throwable {

        mockClassLoader.addResource(MetaInfServicesResourceBuilder
                .service(Module.class)
                .addImplementation(FooModule.class)
                .build());

        assertThat(Thread.currentThread().getContextClassLoader()
                .getResource("META-INF/services/com.google.inject.Module"), is(notNullValue()));
    }

    @When("^I request modules to load$")
    public void i_request_modules_to_load() throws Throwable {
        modules = moduleProvider.get();
    }

    @Then("^I can access the loaded modules$")
    public void i_can_access_the_loaded_modules() throws Throwable {
        assertThat(modules.iterator().next(), is(notNullValue()));
    }


    @When("^the file com\\.google\\.inject\\.Module is removed$")
    public void the_file_com_google_inject_Module_is_removed() throws Throwable {
        mockClassLoader.removeResource("com.google.inject.Module");

        assertThat(Thread.currentThread().getContextClassLoader()
                .getResource("META-INF/services/com.google.inject.Module"), is(nullValue()));
    }

    @Then("^I get the same modules when loading again$")
    public void i_get_the_same_modules_when_loading_again() throws Throwable {
        Collection<Module> cachedModules = moduleProvider.get();

        assertThat(modules.iterator().next(), is(cachedModules.iterator().next()));
    }
}
