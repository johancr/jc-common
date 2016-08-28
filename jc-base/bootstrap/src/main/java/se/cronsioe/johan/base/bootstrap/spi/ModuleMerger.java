package se.cronsioe.johan.base.bootstrap.spi;

import com.google.inject.Module;

import java.util.Collection;

public interface ModuleMerger {

    Module merge(Collection<Module> modules);
}
