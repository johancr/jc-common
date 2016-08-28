package se.cronsioe.johan.test.jpa.impl;

import com.google.inject.Inject;
import se.cronsioe.johan.test.jpa.spi.EntityManagerFactoryFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

public class EntityManagerFactoryFactoryImpl implements EntityManagerFactoryFactory {

    private final PersistenceUnitProvider persistenceUnitProvider;
    private final PersistenceUnitPropertiesProvider persistenceUnitPropertiesProvider;

    @Inject
    public EntityManagerFactoryFactoryImpl(PersistenceUnitProvider persistenceUnitProvider,
                                           PersistenceUnitPropertiesProvider persistenceUnitPropertiesProvider) {
        this.persistenceUnitProvider = persistenceUnitProvider;
        this.persistenceUnitPropertiesProvider = persistenceUnitPropertiesProvider;
    }

    @Override
    public EntityManagerFactory create() {

        String persistenceUnit = persistenceUnitProvider.get();
        Map<String, String> properties = persistenceUnitPropertiesProvider.get();
        return Persistence.createEntityManagerFactory(persistenceUnit, properties);
    }
}
