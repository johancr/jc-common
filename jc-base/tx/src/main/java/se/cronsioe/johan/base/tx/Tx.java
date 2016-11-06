package se.cronsioe.johan.base.tx;

import com.google.inject.ProvidedBy;
import se.cronsioe.johan.base.tx.spi.DefaultTxProvider;

@ProvidedBy(DefaultTxProvider.class)
public interface Tx {

    void add(TxListener listener);

    <T> void bind(Class<T> key, T resource);

    <T> T lookup(Class<T> key);
}
