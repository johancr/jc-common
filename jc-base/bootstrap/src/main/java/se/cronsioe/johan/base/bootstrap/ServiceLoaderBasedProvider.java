package se.cronsioe.johan.base.bootstrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

public abstract class ServiceLoaderBasedProvider<S> {

    private ServiceLoader<S> serviceLoader;

    public Collection<S> get() {
        return load();
    }

    private Collection<S> load() {
        if (serviceLoader == null)
        {
            serviceLoader = ServiceLoader.load(providedClass());
        }

        Collection<S> services = new ArrayList<S>();
        for (S service : serviceLoader)
        {
            services.add(service);
        }
        return services;
    }

    protected abstract Class<S> providedClass();
}
