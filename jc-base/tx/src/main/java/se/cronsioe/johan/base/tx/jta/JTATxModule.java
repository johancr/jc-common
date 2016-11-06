package se.cronsioe.johan.base.tx.jta;


import com.google.inject.AbstractModule;
import se.cronsioe.johan.base.tx.TxScope;

public class JTATxModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(TxScope.class).to(JTATxScope.class);
    }
}
