package se.cronsioe.johan.test.tx.foo;

public class FooSession
{
    private boolean closed = false;

    public void close()
    {
        closed = true;
    }

    public boolean isClosed()
    {
        return closed;
    }
}
