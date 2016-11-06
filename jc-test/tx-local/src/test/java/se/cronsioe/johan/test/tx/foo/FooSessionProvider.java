package se.cronsioe.johan.test.tx.foo;

import com.google.inject.Provider;

public class FooSessionProvider implements Provider<FooSession>
{
    @Override
    public FooSession get()
    {
        return new FooSession();
    }
}
