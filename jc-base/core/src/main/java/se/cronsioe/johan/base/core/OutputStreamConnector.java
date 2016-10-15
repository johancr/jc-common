package se.cronsioe.johan.base.core;

import java.io.*;

public class OutputStreamConnector {

    private final BufferedReader reader;
    private Thread thread;

    public OutputStreamConnector(InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public void waitUntilFinished() {
        try
        {
            thread.join();
        }
        catch (InterruptedException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void pipeTo(final OutputStream outputStream) {

        thread = new Thread() {

            @Override
            public void run() {

                PrintWriter writer = new PrintWriter(outputStream, true);

                try
                {
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        writer.println(line);
                    }
                }
                catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        };

        thread.start();
    }
}
