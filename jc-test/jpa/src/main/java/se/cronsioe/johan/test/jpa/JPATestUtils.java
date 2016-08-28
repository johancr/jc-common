package se.cronsioe.johan.test.jpa;

import com.google.inject.Provider;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JPATestUtils {

    @SuppressWarnings("unchecked")
    public static <T> T makeTransactional(final T service, final Provider<EntityManager> entityManagerProvider) {
        return (T) Proxy.newProxyInstance(service.getClass().getClassLoader(), service.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                EntityManager entityManager = entityManagerProvider.get();
                boolean inActiveTransaction = entityManager.getTransaction().isActive();

                if (inActiveTransaction)
                {
                    return method.invoke(service, args);
                }

                entityManager.getTransaction().begin();
                Object result = method.invoke(service, args);
                entityManager.getTransaction().commit();

                return result;
            }
        });
    }

}
