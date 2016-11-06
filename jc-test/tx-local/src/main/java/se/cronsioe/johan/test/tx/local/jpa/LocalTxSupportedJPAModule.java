package se.cronsioe.johan.test.tx.local.jpa;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

import javax.persistence.EntityManager;

public class LocalTxSupportedJPAModule extends AbstractModule {

    @Override
    protected void configure() {
        LocalTxSupportedEntityManagerInterceptor txInterceptor = new LocalTxSupportedEntityManagerInterceptor();
        requestInjection(txInterceptor);
        bindInterceptor(Matchers.subclassesOf(Provider.class), Matchers.returns(Matchers
                .subclassesOf(EntityManager.class)), txInterceptor);
    }
}
