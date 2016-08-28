package se.cronsioe.johan.base.bootstrap.impl;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import se.cronsioe.johan.base.bootstrap.spi.ModuleMerger;

import java.util.Collection;

public class OverridingModuleMerger implements ModuleMerger {

    @Override
    public Module merge(Collection<Module> modules) {
        Module merged = Modules.EMPTY_MODULE;
        for (Module module : modules)
        {
            merged = Modules.override(merged).with(module);
        }
        return merged;
    }
}
