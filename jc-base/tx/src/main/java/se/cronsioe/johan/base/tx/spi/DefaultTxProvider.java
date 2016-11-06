package se.cronsioe.johan.base.tx.spi;


import com.google.inject.Inject;
import com.google.inject.Provider;
import se.cronsioe.johan.base.tx.Tx;
import se.cronsioe.johan.base.tx.TxScope;

public class DefaultTxProvider implements Provider<Tx> {

    private final TxScope txScope;

    @Inject
    public DefaultTxProvider(TxScope txScope) {
        this.txScope = txScope;
    }

    @Override
    public Tx get() {
        return txScope.get();
    }
}
