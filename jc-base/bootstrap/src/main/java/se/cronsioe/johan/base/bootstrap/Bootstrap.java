package se.cronsioe.johan.base.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import se.cronsioe.johan.base.bootstrap.spi.ModuleMerger;

import java.util.Collection;
import java.util.ServiceConfigurationError;

public class Bootstrap {

    private final Provider<Collection<Module>> moduleProvider;
    private final ModuleMerger moduleMerger;

    private Injector injector;

    public Bootstrap(Provider<Collection<Module>> moduleProvider, ModuleMerger moduleMerger) {
        this.moduleProvider = moduleProvider;
        this.moduleMerger = moduleMerger;
    }

    public <S> S getInstance(Class<S> service) {
        return getInjector().getInstance(service);
    }

    private Injector getInjector() {

        if (injector == null)
        {
            injector = createInjector();
        }

        return injector;
    }

    private Injector createInjector() {
        try
        {
            return Guice.createInjector(loadModules());
        }
        catch (ServiceConfigurationError ex)
        {
            throw new RuntimeException("Could not locate module\n" + ex, ex);
        }
    }

    private Module loadModules() {
        Collection<Module> modules = moduleProvider.get();
        return moduleMerger.merge(modules);
    }
}

