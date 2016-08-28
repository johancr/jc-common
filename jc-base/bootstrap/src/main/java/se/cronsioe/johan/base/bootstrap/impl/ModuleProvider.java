package se.cronsioe.johan.base.bootstrap.impl;

import com.google.inject.Module;
import se.cronsioe.johan.base.bootstrap.spi.ServiceLoaderBasedProvider;

public class ModuleProvider extends ServiceLoaderBasedProvider<Module> {

    @Override
    protected Class<Module> providedClass() {
        return Module.class;
    }
}
