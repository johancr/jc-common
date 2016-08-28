package se.cronsioe.johan.base.bootstrap.spi;

import com.google.inject.Module;
import com.google.inject.Provider;

import java.util.Collection;

public interface ModuleProvider extends Provider<Collection<Module>> {
}
