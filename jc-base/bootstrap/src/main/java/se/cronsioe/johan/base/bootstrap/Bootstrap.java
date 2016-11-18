package se.cronsioe.johan.base.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import se.cronsioe.johan.base.bootstrap.impl.ModuleProvider;
import se.cronsioe.johan.base.bootstrap.impl.OverridingModuleMerger;
import se.cronsioe.johan.base.bootstrap.spi.ModuleMerger;

import java.util.Collection;
import java.util.ServiceConfigurationError;

public class Bootstrap {

    private final Provider<Collection<Module>> modulesProvider;
    private final ModuleMerger moduleMerger;

    private Injector injector;

    public Bootstrap(Provider<Collection<Module>> modulesProvider, ModuleMerger moduleMerger) {
        this.modulesProvider = modulesProvider;
        this.moduleMerger = moduleMerger;
    }

    public static void inject(Object target) {
        DefaultBootstrap.inject(target);
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
            throw new IllegalStateException("Could not create injector: " + ex, ex);
        }
    }

    private Module loadModules() {
        Collection<Module> modules = modulesProvider.get();
        return moduleMerger.merge(modules);
    }

    private static class DefaultBootstrap {

        private static Bootstrap INSTANCE =
                new Bootstrap(new ModuleProvider(), new OverridingModuleMerger());

        public static void inject(Object target) {
            INSTANCE.getInjector().injectMembers(target);
        }
    }
}

