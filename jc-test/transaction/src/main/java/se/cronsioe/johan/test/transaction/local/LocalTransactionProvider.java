package se.cronsioe.johan.test.transaction.local;

import com.google.inject.Inject;
import com.google.inject.Provider;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.base.transaction.TransactionException;

public class LocalTransactionProvider implements Provider<Transaction> {

    private final LocalTransactionManager transactionManager;

    @Inject
    public LocalTransactionProvider(LocalTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Transaction get() {

        Transaction transaction = transactionManager.get();

        if (transaction != null)
        {
            return transaction;
        }

        throw new TransactionException("No active transaction");
    }

}
