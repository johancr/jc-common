package se.cronsioe.johan.base.bootstrap.spi;

import com.google.inject.Provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

public abstract class ServiceLoaderBasedProvider<S> implements Provider<Collection<S>> {

    private ServiceLoader<S> serviceLoader;

    @Override
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
