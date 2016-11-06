package se.cronsioe.johan.test.tx.foo;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

public class FooModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FooSession.class).toProvider(FooSessionProvider.class);

        TxSupportedFooSessionInterceptor txInterceptor = new TxSupportedFooSessionInterceptor();
        requestInjection(txInterceptor);
        bindInterceptor(Matchers.subclassesOf(Provider.class), Matchers.returns(Matchers.subclassesOf(FooSession.class)), txInterceptor);
    }
}
