package se.cronsioe.johan.test.tx.local;


import com.google.inject.AbstractModule;
import se.cronsioe.johan.base.tx.TxScope;

public class LocalTxModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TxScope.class).to(LocalTxScope.class);
    }
}
