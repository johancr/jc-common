package se.cronsioe.johan.test.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class JPAExecutor {

    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public JPAExecutor(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    public <T> T execute(JPATask<T> task) {
        EntityManager entityManager = entityManagerProvider.get();

        if (entityManager.getTransaction().isActive())
        {
            return task.run(entityManager);
        }
        else
        {
            entityManager.getTransaction().begin();
            T result = task.run(entityManager);
            entityManager.getTransaction().commit();
            return result;
        }
    }
}
