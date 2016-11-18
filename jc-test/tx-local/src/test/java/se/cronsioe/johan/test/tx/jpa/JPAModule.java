package se.cronsioe.johan.test.tx.jpa;

import com.google.inject.AbstractModule;
import se.cronsioe.johan.test.jpa.guice.JPATestModule;

public class JPAModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JPATestModule());
    }
}
