package se.cronsioe.johan.test.tx.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerProvider implements Provider<EntityManager> {

    private final Provider<EntityManagerFactory> entityManagerFactoryProvider;

    @Inject
    public EntityManagerProvider(Provider<EntityManagerFactory> entityManagerFactoryProvider) {
        this.entityManagerFactoryProvider = entityManagerFactoryProvider;
    }

    @Override
    public EntityManager get() {
        return newEntityManager();
    }

    private EntityManager newEntityManager() {
        return entityManagerFactory().createEntityManager();
    }

    private EntityManagerFactory entityManagerFactory() {
        return entityManagerFactoryProvider.get();
    }
}
