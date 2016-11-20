package se.cronsioe.johan.test.tx.jpa;

import com.google.inject.AbstractModule;
import se.cronsioe.johan.test.jpa.guice.JPATestModule;
import se.cronsioe.johan.test.tx.local.jpa.LocalTxSupportedJPAModule;

public class JPAModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JPATestModule());
        install(new LocalTxSupportedJPAModule());
    }
}
