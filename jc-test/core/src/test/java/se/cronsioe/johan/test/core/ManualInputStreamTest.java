package se.cronsioe.johan.test.core;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ManualInputStreamTest {

    @Test
    public void manual() throws IOException {
        ManualInputStream inputStream = new ManualInputStream();

        inputStream.feed("test\n");
        String line = new BufferedReader(new InputStreamReader(inputStream)).readLine();

        assertThat(line, is("test"));
    }


    @Test
    public void readWaitsForData() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final ManualInputStream inputStream = new ManualInputStream();
        Runnable feed = new Runnable() {
            @Override
            public void run() {
                inputStream.feed("test\n");
            }
        };
        Callable<String> readStream = new Callable<String>() {

            @Override
            public String call() throws Exception {
                try
                {
                    return new BufferedReader(new InputStreamReader(inputStream)).readLine();
                }
                catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        };

        Future<String> line = executorService.submit(readStream);
        executorService.submit(feed);

        assertThat(line.get(), is("test"));
    }
}
