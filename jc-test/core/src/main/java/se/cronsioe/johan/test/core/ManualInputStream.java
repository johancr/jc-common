package se.cronsioe.johan.test.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ManualInputStream extends InputStream {

    private final BlockingQueue<Byte> queue = new ArrayBlockingQueue<Byte>(100);
    private boolean finished = false;

    public void feed(String input) {
        try
        {
            for (byte b : input.getBytes())
            {
                queue.put(b);
            }

            queue.put((byte) -1);
        }
        catch (InterruptedException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int read() throws IOException {

        if (finished)
        {
            return -1;
        }

        try
        {
            int read = queue.take();

            if (read == -1)
            {
                finished = true;
            }

            return read;
        }
        catch (InterruptedException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
