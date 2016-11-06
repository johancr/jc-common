package se.cronsioe.johan.base.tx;


public interface TxScope {

    Tx begin();

    void commit();

    void rollback();

    Tx get();
}
