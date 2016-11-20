package se.cronsioe.johan.test.tx.local;

import com.google.inject.Singleton;
import se.cronsioe.johan.base.tx.Tx;
import se.cronsioe.johan.base.tx.TxException;
import se.cronsioe.johan.base.tx.TxListener;
import se.cronsioe.johan.base.tx.TxScope;
import se.cronsioe.johan.base.tx.spi.ManagedTx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class LocalTxScope implements TxScope {

    private ManagedTx tx;

    @Override
    public Tx begin() {
        if (tx == null)
        {
            tx = create();
        }

        return tx;
    }

    private static ManagedTx create() {
        return new ManagedTx() {
            private final Collection<TxListener> listeners = new ArrayList<TxListener>();
            private final Map<Class<?>, Object> resources = new HashMap<Class<?>, Object>();

            @Override
            public void add(TxListener listener) {
                listeners.add(listener);
            }

            @Override
            public <T> void bind(Class<T> key, T resource) {
                resources.put(key, resource);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> T lookup(Class<T> key) {
                return (T) resources.get(key);
            }

            @Override
            public void commit() {
                for (TxListener listener : listeners)
                {
                    listener.onCommit();
                }
                end();
            }

            private void end() {
                for (TxListener listener : listeners)
                {
                    listener.onEnd();
                }
            }

            @Override
            public void rollback() {
                for (TxListener listener : listeners)
                {
                    listener.onRollback();
                }
                end();
            }
        };
    }

    @Override
    public void commit() {
        try
        {
            doGet().commit();
        }
        catch (Throwable ex)
        {
            // do nothing
        }
        end();
    }

    @Override
    public void rollback() {
        try
        {
            doGet().rollback();
        }
        catch (Throwable ex)
        {
            // do nothing
        }
        end();
    }

    @Override
    public Tx get() {
        return doGet();
    }

    private ManagedTx doGet() {
        if (tx == null)
        {
            throw new TxException("No local transaction available");
        }

        return tx;
    }

    private void end() {
        tx = null;
    }
}
