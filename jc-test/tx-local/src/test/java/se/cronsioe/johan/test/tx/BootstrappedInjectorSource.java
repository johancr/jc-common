package se.cronsioe.johan.test.tx;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

import java.util.ServiceLoader;

public class BootstrappedInjectorSource implements InjectorSource {

    @Override
    public Injector getInjector() {
        return Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO, loadModule());
    }

    private Module loadModule() {
        Module result = Modules.EMPTY_MODULE;

        for (Module module : ServiceLoader.load(Module.class))
        {
            result = Modules.override(module).with(result);
        }

        return result;
    }
}
