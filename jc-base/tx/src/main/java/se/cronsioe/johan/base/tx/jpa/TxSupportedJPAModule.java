package se.cronsioe.johan.base.tx.jpa;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

import javax.persistence.EntityManager;

public class TxSupportedJPAModule extends AbstractModule {

    @Override
    protected void configure() {
        TxSupportedEntityManagerInterceptor txInterceptor = new TxSupportedEntityManagerInterceptor();
        requestInjection(txInterceptor);
        bindInterceptor(Matchers.subclassesOf(Provider.class), Matchers.returns(Matchers
                .subclassesOf(EntityManager.class)), txInterceptor);
    }
}
