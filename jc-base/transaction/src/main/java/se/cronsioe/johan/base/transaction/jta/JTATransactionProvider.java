package se.cronsioe.johan.base.transaction.jta;

import com.google.inject.Inject;
import com.google.inject.Provider;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.base.transaction.TransactionException;
import se.cronsioe.johan.base.transaction.TransactionListener;

import javax.naming.Context;
import javax.transaction.Status;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public class JTATransactionProvider implements Provider<Transaction> {

    private final Provider<Context> contextProvider;

    @Inject
    public JTATransactionProvider(Provider<Context> contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Override
    public Transaction get() {
        Context context = contextProvider.get();

        try
        {
            TransactionManager transactionManager = (TransactionManager) context.lookup("java:comp/TransactionManager");
            final javax.transaction.Transaction transaction = transactionManager.getTransaction();
            final TransactionSynchronizationRegistry registry =
                    (TransactionSynchronizationRegistry) context.lookup("java:comp/TransactionSynchronizationRegistry");

            return new Transaction() {

                @Override
                public void add(final TransactionListener listener) {
                    try
                    {
                        transaction.enlistResource(asXAResource(listener));
                    }
                    catch (Exception ex)
                    {
                        throw new TransactionException("Could not add transaction listener", ex);
                    }
                }

                @Override
                public void commit() {
                    throw new UnsupportedOperationException("User can not commit JTA transaction");
                }

                @Override
                public boolean isActive() {
                    try
                    {
                        return Status.STATUS_ACTIVE == transaction.getStatus();
                    }
                    catch (Exception ex)
                    {
                        throw new TransactionException("Could not check transaction status", ex);
                    }
                }

                @Override
                @SuppressWarnings("unchecked")
                public <T> T lookup(Class<T> klass) {
                    return (T) registry.getResource(klass);
                }

                @Override
                public <T> void bind(Class<T> klass, T object) {
                    registry.putResource(klass, object);
                }
            };
        }
        catch (Exception ex)
        {
            throw new TransactionException("Could not get transaction", ex);
        }
    }

    private static XAResource asXAResource(final TransactionListener listener) {
        return new XAResource() {
            @Override
            public void commit(Xid xid, boolean b) throws XAException {
                listener.onCommit();
            }

            @Override
            public void end(Xid xid, int i) throws XAException {

            }

            @Override
            public void forget(Xid xid) throws XAException {

            }

            @Override
            public int getTransactionTimeout() throws XAException {
                return 0;
            }

            @Override
            public boolean isSameRM(XAResource xaResource) throws XAException {
                return false;
            }

            @Override
            public int prepare(Xid xid) throws XAException {
                return 0;
            }

            @Override
            public Xid[] recover(int i) throws XAException {
                return new Xid[0];
            }

            @Override
            public void rollback(Xid xid) throws XAException {

            }

            @Override
            public boolean setTransactionTimeout(int i) throws XAException {
                return false;
            }

            @Override
            public void start(Xid xid, int i) throws XAException {

            }
        };
    }
}
