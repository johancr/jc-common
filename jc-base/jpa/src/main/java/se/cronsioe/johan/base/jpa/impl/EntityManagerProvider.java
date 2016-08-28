package se.cronsioe.johan.base.jpa.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.base.transaction.TransactionListener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TransactionRequiredException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Singleton
public class EntityManagerProvider implements Provider<EntityManager> {

    private final Provider<EntityManagerFactory> entityManagerFactoryProvider;
    private final Provider<Transaction> transactionProvider;

    private EntityManager proxy;

    @Inject
    public EntityManagerProvider(Provider<EntityManagerFactory> entityManagerFactoryProvider,
                                 Provider<Transaction> transactionProvider) {
        this.entityManagerFactoryProvider = entityManagerFactoryProvider;
        this.transactionProvider = transactionProvider;
    }

    @Override
    public EntityManager get() {

        EntityManager entityManager = transaction().lookup(EntityManager.class);

        if (nullOrClosed(entityManager))
        {
            entityManager = entityManagerFactory().createEntityManager();

            joinTransaction(entityManager);

            proxy = (EntityManager) Proxy.newProxyInstance(getClass().getClassLoader(), entityManager.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    EntityManager entityManager = transaction().lookup(EntityManager.class);

                    if (nullOrClosed(entityManager))
                    {
                        entityManager = entityManagerFactory().createEntityManager();
                        joinTransaction(entityManager);
                    }

                    return method.invoke(entityManager, args);
                }
            });
        }
        return proxy;
    }

    private void joinTransaction(final EntityManager entityManager) {

        try
        {
            // Join JTA transaction if in application container
            entityManager.joinTransaction();
        }
        catch (TransactionRequiredException ex)
        {
            // Running in unit test
            entityManager.getTransaction().begin();

            transaction().add(new TransactionListener() {
                @Override
                public void onCommit() {
                    entityManager.getTransaction().commit();
                }
            });
        }

        transaction().bind(EntityManager.class, entityManager);
    }

    private Transaction transaction() {
        return transactionProvider.get();
    }

    private boolean nullOrClosed(EntityManager entityManager) {
        return entityManager == null || !entityManager.isOpen();
    }

    private EntityManagerFactory entityManagerFactory() {
        return entityManagerFactoryProvider.get();
    }

}

