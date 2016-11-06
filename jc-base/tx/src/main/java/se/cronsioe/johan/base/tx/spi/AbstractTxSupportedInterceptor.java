package se.cronsioe.johan.base.tx.spi;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import se.cronsioe.johan.base.tx.Tx;

public abstract class AbstractTxSupportedInterceptor<T> implements MethodInterceptor {

    @Inject
    private Provider<Tx> txProvider;

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Tx tx = tx();
        T resource = tx.lookup(resourceClass());

        if (hasExpired(resource))
        {
            resource = (T) invocation.proceed();
            postCreate(resource, tx);
            tx.bind(resourceClass(), resource);
        }

        return resource;
    }

    private Tx tx() {
        return txProvider.get();
    }

    protected abstract Class<T> resourceClass();

    protected boolean hasExpired(T resource) {
        return resource == null;
    }

    protected void postCreate(T resource, Tx tx) {
    }
}
