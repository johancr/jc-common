package se.cronsioe.johan.base.tx.spi;


import se.cronsioe.johan.base.tx.Tx;

public interface ManagedTx extends Tx {

    void commit();

    void rollback();
}
