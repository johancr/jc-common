package se.cronsioe.johan.base.jpa.impl;

import com.google.inject.Provider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

    private EntityManagerFactory entityManagerFactory;

    private final String persistenceUnit;

    public EntityManagerFactoryProvider(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    @Override
    public EntityManagerFactory get() {

        if (entityManagerFactory == null || !entityManagerFactory.isOpen())
        {
            entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
        }
        return entityManagerFactory;
    }
}
