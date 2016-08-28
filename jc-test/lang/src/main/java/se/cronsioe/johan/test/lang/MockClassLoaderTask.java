package se.cronsioe.johan.test.lang;

import java.util.ArrayList;
import java.util.Collection;

public abstract class MockClassLoaderTask {

    private Collection<Resource> resources = new ArrayList<Resource>();

    protected void addResource(Resource resource) {
        resources.add(resource);
    }

    public Collection<Resource> getResources() {
        return resources;
    }

    public abstract void configure(MockClassLoader mockClassLoader);

    public abstract void run();
}
