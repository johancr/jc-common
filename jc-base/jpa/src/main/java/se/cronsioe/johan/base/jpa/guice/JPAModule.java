package se.cronsioe.johan.base.jpa.guice;

import com.google.inject.AbstractModule;
import se.cronsioe.johan.base.jpa.impl.EntityManagerFactoryProvider;
import se.cronsioe.johan.base.jpa.impl.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JPAModule extends AbstractModule {

    private final String persistenceUnit;

    public JPAModule(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    @Override
    protected void configure() {
        bind(EntityManagerFactory.class).toProvider(new EntityManagerFactoryProvider(persistenceUnit));
        bind(EntityManager.class).toProvider(EntityManagerProvider.class);
    }
}
