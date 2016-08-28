package se.cronsioe.johan.base.transaction.jta;

import com.google.inject.Provider;
import se.cronsioe.johan.base.transaction.TransactionException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class InitialContextProvider implements Provider<Context> {

    private static final ThreadLocal<Context> INSTANCE = initialize();

    @Override
    public Context get() {
        return INSTANCE.get();
    }

    private static final ThreadLocal<Context> initialize() {
        return new ThreadLocal<Context>() {

            @Override
            protected Context initialValue() {
                try
                {
                    return new InitialContext();
                }
                catch (NamingException ex)
                {
                    throw new TransactionException("Could not get initial context", ex);
                }
            }
        };
    }
}
