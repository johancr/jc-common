package se.cronsioe.johan.test.jpa.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

@Singleton
public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

    private final PersistenceUnitProvider persistenceUnitProvider;
    private final PersistenceUnitPropertiesProvider persistenceUnitPropertiesProvider;

    private EntityManagerFactory entityManagerFactory;

    @Inject
    public EntityManagerFactoryProvider(PersistenceUnitProvider persistenceUnitProvider,
                                        PersistenceUnitPropertiesProvider persistenceUnitPropertiesProvider) {
        this.persistenceUnitProvider = persistenceUnitProvider;
        this.persistenceUnitPropertiesProvider = persistenceUnitPropertiesProvider;
    }

    @Override
    public EntityManagerFactory get() {

        if (entityManagerFactory == null || !entityManagerFactory.isOpen())
        {
            entityManagerFactory = create();
        }
        return entityManagerFactory;
    }

    private EntityManagerFactory create() {
        String persistenceUnit = persistenceUnitProvider.get();
        Map<String, String> properties = persistenceUnitPropertiesProvider.get();
        return Persistence.createEntityManagerFactory(persistenceUnit, properties);
    }
}
