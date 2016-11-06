package se.cronsioe.johan.base.tx;

public interface TxListener {

    void onCommit();

    void onRollback();

    void onEnd();
}
