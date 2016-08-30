package se.cronsioe.johan.test.lang;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

public class MockClassLoader extends ClassLoader {

    private Collection<Resource> resources = new ArrayList<Resource>();
    private ResourcePathResolver resourcePathResolver = new ResourcePathResolver();

    public MockClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    public MockClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Collection<URL> result = new ArrayList<URL>();

        for (final Resource resource : resources)
        {
            String path = resourcePathResolver.resolve(resource);
            if (path.equals(name))
            {
                try
                {
                    result.add(createURL(resource, path));
                }
                catch (MalformedURLException ex)
                {
                    throw new IllegalStateException("Could not create url for resource:" + ex, ex);
                }
            }
        }

        result.addAll(Collections.list(super.getResources(name)));
        return Collections.enumeration(result);
    }

    @Override
    public URL getResource(String name) {
        for (final Resource resource : resources)
        {
            String path = resourcePathResolver.resolve(resource);
            if (path.equals(name))
            {
                try
                {
                    return createURL(resource, path);
                }
                catch (MalformedURLException ex)
                {
                    throw new IllegalStateException("Could not create url for resource:" + ex, ex);
                }
            }
        }

        return super.getResource(name);
    }

    private URL createURL(final Resource resource, String path) throws MalformedURLException {
        return new URL("file", "localhost", 1024, path, new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL url) throws IOException {
                return new URLConnection(url) {
                    @Override
                    public void connect() throws IOException {

                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return new ByteArrayInputStream(resource.getContent().getBytes());
                    }
                };
            }
        });
    }


    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void removeResource(String name) {
        Collection<Resource> toBeRemoved = new ArrayList<Resource>();
        for (Resource resource : resources)
        {
            if (resource.getName().equals(name))
            {
                toBeRemoved.add(resource);
            }
        }

        resources.removeAll(toBeRemoved);
    }
}
