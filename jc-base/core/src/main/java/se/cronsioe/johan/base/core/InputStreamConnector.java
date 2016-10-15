package se.cronsioe.johan.base.core;

import java.io.*;

public class InputStreamConnector {

    private final PrintWriter writer;
    private Thread thread;

    public InputStreamConnector(OutputStream outputStream) {
        this.writer = new PrintWriter(outputStream, true);
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

    public void pipeFrom(final InputStream inputStream) {

        thread = new Thread() {

            @Override
            public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

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
