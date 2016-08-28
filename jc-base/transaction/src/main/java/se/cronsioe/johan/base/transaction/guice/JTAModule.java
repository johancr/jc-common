package se.cronsioe.johan.base.transaction.guice;

import com.google.inject.AbstractModule;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.base.transaction.jta.InitialContextProvider;
import se.cronsioe.johan.base.transaction.jta.JTATransactionProvider;

import javax.naming.Context;

public class JTAModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Context.class).toProvider(InitialContextProvider.class);
        bind(Transaction.class).toProvider(JTATransactionProvider.class);
    }
}
