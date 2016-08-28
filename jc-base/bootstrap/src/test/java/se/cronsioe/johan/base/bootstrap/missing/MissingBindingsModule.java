package se.cronsioe.johan.base.bootstrap.missing;

import com.google.inject.AbstractModule;

public class MissingBindingsModule extends AbstractModule {

    @Override
    protected void configure() {
        try
        {
            bind(MissingService.class).to((Class<? extends MissingService>) Class.forName("missingImplementation"));
        }
        catch (ClassNotFoundException ex)
        {
            throw new RuntimeException("Could not bind to implementation", ex);
        }
    }
}
