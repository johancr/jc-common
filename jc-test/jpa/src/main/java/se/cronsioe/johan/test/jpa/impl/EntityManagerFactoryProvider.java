package se.cronsioe.johan.test.jpa.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import se.cronsioe.johan.test.jpa.spi.EntityManagerFactoryFactory;

import javax.persistence.EntityManagerFactory;

@Singleton
public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

    private final EntityManagerFactoryFactory entityManagerFactoryFactory;

    private EntityManagerFactory entityManagerFactory;

    @Inject
    public EntityManagerFactoryProvider(EntityManagerFactoryFactory entityManagerFactoryFactory) {
        this.entityManagerFactoryFactory = entityManagerFactoryFactory;
    }

    @Override
    public EntityManagerFactory get() {

        if (entityManagerFactory == null || !entityManagerFactory.isOpen())
        {
            entityManagerFactory = entityManagerFactoryFactory.create();
        }
        return entityManagerFactory;
    }
}
