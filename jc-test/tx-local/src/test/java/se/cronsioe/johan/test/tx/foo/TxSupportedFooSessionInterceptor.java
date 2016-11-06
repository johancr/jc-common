package se.cronsioe.johan.test.tx.foo;


import se.cronsioe.johan.base.tx.Tx;
import se.cronsioe.johan.base.tx.spi.AbstractTxListener;
import se.cronsioe.johan.base.tx.spi.AbstractTxSupportedInterceptor;

public class TxSupportedFooSessionInterceptor extends AbstractTxSupportedInterceptor<FooSession> {

    @Override
    protected Class<FooSession> resourceClass() {
        return FooSession.class;
    }

    @Override
    protected boolean hasExpired(FooSession session) {
        return session == null || session.isClosed();
    }

    @Override
    protected void postCreate(final FooSession session, Tx tx) {
        tx.add(new AbstractTxListener() {
            @Override
            public void onEnd() {
                session.close();
            }
        });
    }
}
