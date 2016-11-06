package se.cronsioe.johan.test.tx.jpa;

import com.google.inject.AbstractModule;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JPAModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class);
        bind(EntityManagerFactory.class).toProvider(new EntityManagerFactoryProvider("foo-unit"));
    }
}
