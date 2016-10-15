package se.cronsioe.johan.base.core;

import org.junit.Test;
import se.cronsioe.johan.test.core.ManualInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InputStreamConnectorTest {

    @Test
    public void pipeFrom() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStreamConnector connector = new InputStreamConnector(out);
        InputStream stream = new ByteArrayInputStream("test".getBytes());

        connector.pipeFrom(stream);
        connector.waitUntilFinished();

        assertThat(out.toString(), is("test\n"));
    }

    @Test
    public void pipeFrom_asynchronous() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStreamConnector connector = new InputStreamConnector(out);
        ManualInputStream stream = new ManualInputStream();

        connector.pipeFrom(stream);
        stream.feed("test\n");
        connector.waitUntilFinished();

        assertThat(out.toString(), is("test\n"));
    }
}
