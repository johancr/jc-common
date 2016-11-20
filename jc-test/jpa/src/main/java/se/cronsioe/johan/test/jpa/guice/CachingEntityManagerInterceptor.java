package se.cronsioe.johan.test.jpa.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.persistence.EntityManager;

public class CachingEntityManagerInterceptor implements MethodInterceptor {

    private EntityManager entityManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        if (nullOrClosed(entityManager))
        {
            entityManager = (EntityManager) invocation.proceed();
        }

        return entityManager;
    }

    private boolean nullOrClosed(EntityManager entityManager) {
        return entityManager == null || !entityManager.isOpen();
    }
}
