package se.cronsioe.johan.test.transaction.local;

import com.google.inject.Singleton;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.base.transaction.TransactionException;
import se.cronsioe.johan.base.transaction.TransactionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Singleton
public class LocalTransactionManager {

    private Transaction transaction;

    public void begin() {

        if (transaction != null && transaction.isActive())
        {
            throw new TransactionException("Can't begin transaction, one already exists");
        }

        transaction = create();
    }

    private static Transaction create() {
        return new Transaction() {

            private final Collection<TransactionListener> listeners = new ArrayList<TransactionListener>();
            private final Map<Class<?>, Object> registry = new HashMap<Class<?>, Object>();
            private boolean active = true;

            @Override
            public void add(TransactionListener listener) {
                listeners.add(listener);
            }

            @Override
            public void commit() {
                for (TransactionListener listener : listeners)
                {
                    try
                    {
                        listener.onCommit();
                    }
                    catch (Exception ex)
                    {
                        // do nothing
                    }
                }
                active = false;
            }

            @Override
            public boolean isActive() {
                return active;
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> T lookup(Class<T> klass) {
                return (T) registry.get(klass);
            }

            @Override
            public <T> void bind(Class<T> klass, T object) {
                registry.put(klass, object);
            }
        };
    }

    public void commit() {
        Transaction transaction = getTransaction();

        if (transaction.isActive())
        {
            transaction.commit();
        }
        else
        {
            throw new TransactionException("Can not commit transaction which is not active");
        }
    }

    public Transaction getTransaction() {

        if (transaction != null)
        {
            return transaction;
        }

        throw new TransactionException("No transaction available");
    }
}
