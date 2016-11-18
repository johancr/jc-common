package se.cronsioe.johan.test.jpa.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.persistence.EntityManager;

public class ThreadBoundEntityManagerInterceptor implements MethodInterceptor {

    @Inject
    private Provider<EntityManager> entityManagerProvider;

    private static final ThreadLocal<EntityManager> LOCAL = new ThreadLocal<EntityManager>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        EntityManager entityManager = LOCAL.get();

        if (nullOrClosed(entityManager))
        {
            LOCAL.set(entityManager = (EntityManager) invocation.proceed());
        }

        return entityManager;
    }

    private boolean nullOrClosed(EntityManager entityManager) {
        return entityManager == null || !entityManager.isOpen();
    }
}
