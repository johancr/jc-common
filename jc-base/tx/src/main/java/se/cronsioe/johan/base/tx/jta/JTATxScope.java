package se.cronsioe.johan.base.tx.jta;

import com.google.inject.Inject;
import com.google.inject.Provider;
import se.cronsioe.johan.base.tx.Tx;
import se.cronsioe.johan.base.tx.TxException;
import se.cronsioe.johan.base.tx.TxListener;
import se.cronsioe.johan.base.tx.TxScope;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class JTATxScope implements TxScope
{
    private final Provider<InitialContext> initialContextProvider;

    @Inject
    public JTATxScope(Provider<InitialContext> initialContextProvider)
    {
        this.initialContextProvider = initialContextProvider;
    }

    public JTATxScope()
    {
        this(new InitialContextProvider());
    }

    @Override
    public Tx get()
    {
        try
        {
            TransactionManager tm = getTransactionManager();
            final Transaction transaction = tm.getTransaction();
            final TransactionSynchronizationRegistry registry = getRegistry();

            return new Tx()
            {
                @Override
                public void add(TxListener listener)
                {
                    try
                    {
                        transaction.enlistResource(createXAResource(listener));
                    }
                    catch (Exception ex)
                    {
                        throw new TxException(
                            "Could not bind resource to JTA transaction: " + ex, ex);
                    }
                }

                @Override
                public <T> void bind(Class<T> key, T resource)
                {
                    registry.putResource(key, resource);
                }

                @Override
                @SuppressWarnings("unchecked")
                public <T> T lookup(Class<T> key)
                {
                    return (T) registry.getResource(key);
                }
            };
        }
        catch (Exception ex)
        {
            throw new TxException("Could not get JTA transaction: " + ex, ex);
        }
    }

    private TransactionManager getTransactionManager() throws NamingException
    {
        InitialContext context = initialContextProvider.get();
        return (TransactionManager) context.lookup("java:comp/TransactionManager");
    }

    private TransactionSynchronizationRegistry getRegistry() throws NamingException
    {
        InitialContext context = initialContextProvider.get();
        return (TransactionSynchronizationRegistry) context
            .lookup("java:comp/TransactionSynchronizationRegistry");
    }

    private XAResource createXAResource(final TxListener listener)
    {
        return new XAResource()
        {
            @Override
            public void commit(Xid arg0, boolean arg1) throws XAException
            {
                listener.onCommit();
            }

            @Override
            public void end(Xid arg0, int arg1) throws XAException
            {
                listener.onEnd();
            }

            @Override
            public void forget(Xid arg0) throws XAException
            {
            }

            @Override
            public int getTransactionTimeout() throws XAException
            {
                return 0;
            }

            @Override
            public boolean isSameRM(XAResource arg0) throws XAException
            {
                return false;
            }

            @Override
            public int prepare(Xid arg0) throws XAException
            {
                return 0;
            }

            @Override
            public Xid[] recover(int arg0) throws XAException
            {
                return null;
            }

            @Override
            public void rollback(Xid arg0) throws XAException
            {
                listener.onRollback();
            }

            @Override
            public boolean setTransactionTimeout(int arg0) throws XAException
            {
                return false;
            }

            @Override
            public void start(Xid arg0, int arg1) throws XAException
            {
            }
        };
    }

    private static class InitialContextProvider implements Provider<InitialContext>
    {
        private static final ThreadLocal<InitialContext> LOCAL_INITIAL_CONTEXT = new ThreadLocal<InitialContext>()
        {
            @Override
            protected InitialContext initialValue()
            {
                try
                {
                    return new InitialContext();
                }
                catch (Exception ex)
                {
                    throw new IllegalStateException("Could not create context: " + ex, ex);
                }
            }
        };

        @Override
        public InitialContext get()
        {
            return LOCAL_INITIAL_CONTEXT.get();
        }
    }

    @Override
    public Tx begin()
    {
        throw new UnsupportedOperationException("Begin may not be called explicitly");
    }

    @Override
    public void commit()
    {
        throw new UnsupportedOperationException("Commit may not be called explicitly");
    }

    @Override
    public void rollback()
    {
        throw new UnsupportedOperationException("Rollback may not be called explicitly");
    }
}
