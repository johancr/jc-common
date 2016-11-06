package se.cronsioe.johan.base.tx.spi;

import se.cronsioe.johan.base.tx.TxListener;

public abstract class AbstractTxListener implements TxListener {

    @Override
    public void onCommit() {
    }

    @Override
    public void onRollback() {
    }

    @Override
    public void onEnd() {
    }
}
