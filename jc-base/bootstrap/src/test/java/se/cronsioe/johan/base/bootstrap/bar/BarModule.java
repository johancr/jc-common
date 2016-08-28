package se.cronsioe.johan.base.bootstrap.bar;

import com.google.inject.AbstractModule;

public class BarModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BarService.class).to(BarServiceImpl.class);
    }
}
