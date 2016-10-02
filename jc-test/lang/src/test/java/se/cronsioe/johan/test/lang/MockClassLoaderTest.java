package se.cronsioe.johan.test.lang;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static se.cronsioe.johan.test.junit.matchers.InputStreamMatchers.streamContains;

public class MockClassLoaderTest {

    @Test
    public void getResource() throws IOException {
        MockClassLoader mockClassLoader = new MockClassLoader();
        Resource resource = Resource.named("test.txt").withContent("hello").build();
        mockClassLoader.addResource(resource);

        URL url = mockClassLoader.getResource("test.txt");

        assertThat(url.getPath(), is("test.txt"));
        assertThat(url.openStream(), streamContains("hello"));
    }

    @Test
    public void unknownResourceIsNull() throws IOException {
        MockClassLoader mockClassLoader = new MockClassLoader();

        URL url = mockClassLoader.getResource("test.txt");

        assertThat(url, is(nullValue()));
    }

    @Test
    public void loadsMetaInfResource() throws IOException {
        MockClassLoader mockClassLoader = new MockClassLoader();
        Resource resource = Resource.named("persistence.xml").withContent("persistence stuff").inDirectory("META-INF").build();
        mockClassLoader.addResource(resource);

        URL url = mockClassLoader.getResource("META-INF/persistence.xml");

        assertThat(url.getPath(), is("META-INF/persistence.xml"));
        assertThat(url.openStream(), streamContains("persistence stuff"));
    }

    @Test
    public void getResourceFromParentClassLoader() throws IOException {
        MockClassLoader parentClassLoader = new MockClassLoader();
        Resource resource = Resource.named("test.txt").withContent("some text").build();
        parentClassLoader.addResource(resource);

        MockClassLoader mockClassLoader = new MockClassLoader(parentClassLoader);
        URL url = mockClassLoader.getResource("test.txt");

        assertThat(url.getPath(), is("test.txt"));
        assertThat(url.openStream(), streamContains("some text"));
    }

    @Test
    public void removeResource() throws IOException {
        MockClassLoader mockClassLoader = new MockClassLoader();
        Resource resource = Resource.named("test.txt").withContent("hello").build();
        mockClassLoader.addResource(resource);

        mockClassLoader.removeResource("test.txt");

        URL url = mockClassLoader.getResource("test.txt");
        assertThat(url, is(nullValue()));
    }
}
