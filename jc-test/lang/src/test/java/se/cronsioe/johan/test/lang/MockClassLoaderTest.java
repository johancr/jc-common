package se.cronsioe.johan.test.lang;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

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

    private static Matcher<InputStream> streamContains(final String expected) {
        return new TypeSafeMatcher<InputStream>() {
            @Override
            protected boolean matchesSafely(InputStream actual) {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(actual));

                try
                {
                    while ((line = reader.readLine()) != null)
                    {
                        if (line.contains(expected))
                        {
                            return true;
                        }
                    }
                }
                catch (Exception ex)
                {
                    throw new RuntimeException(ex);
                }

                return false;
            }

            @Override
            protected void describeMismatchSafely(InputStream item, Description mismatchDescription) {
                mismatchDescription.appendText("but it didn't");
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("inputstream contains \"" + expected + "\"");
            }
        };
    }
}
