package se.cronsioe.johan.test.transaction.guice;

import com.google.inject.AbstractModule;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.test.transaction.local.LocalTransactionProvider;

public class LocalTransactionModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(Transaction.class).toProvider(LocalTransactionProvider.class);
    }
}
