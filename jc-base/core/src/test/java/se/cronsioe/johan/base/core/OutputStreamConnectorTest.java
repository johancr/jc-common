package se.cronsioe.johan.base.core;

import org.junit.Test;
import se.cronsioe.johan.test.core.ManualInputStream;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OutputStreamConnectorTest {

    @Test
    public void pipeTo() {
        ManualInputStream inputStream = new ManualInputStream();
        OutputStreamConnector connector = new OutputStreamConnector(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        connector.pipeTo(outputStream);

        inputStream.feed("test");
        connector.waitUntilFinished();

        assertThat(outputStream.toString(), is("test\n"));
    }

    @Test
    public void pipeTo_newLineIgnored() {
        ManualInputStream inputStream = new ManualInputStream();
        OutputStreamConnector connector = new OutputStreamConnector(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        connector.pipeTo(outputStream);

        inputStream.feed("test\n");
        connector.waitUntilFinished();

        assertThat(outputStream.toString(), is("test\n"));
    }
}
