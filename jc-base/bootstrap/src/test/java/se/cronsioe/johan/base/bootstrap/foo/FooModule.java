package se.cronsioe.johan.base.bootstrap.foo;

import com.google.inject.AbstractModule;

public class FooModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FooService.class).to(FooServiceImpl.class);
    }
}
