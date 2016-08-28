package se.cronsioe.johan.base.bootstrap.impl;

import com.google.inject.Module;
import se.cronsioe.johan.base.bootstrap.ServiceLoaderBasedProvider;
import se.cronsioe.johan.base.bootstrap.spi.ModuleProvider;

public class ModuleProviderImpl extends ServiceLoaderBasedProvider<Module> implements ModuleProvider {

    @Override
    protected Class<Module> providedClass() {
        return Module.class;
    }
}
