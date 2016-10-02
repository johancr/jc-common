package se.cronsioe.johan.test.lang;

public class MockClassLoaderExecutor {

    public void execute(MockClassLoaderTask task) {

        MockClassLoader mockClassLoader = createMockClassLoader(task);

        ClassLoader current = Thread.currentThread().getContextClassLoader();

        try
        {
            Thread.currentThread().setContextClassLoader(mockClassLoader);
            task.run();
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(current);
        }
    }

    private MockClassLoader createMockClassLoader(MockClassLoaderTask task) {
        MockClassLoader mockClassLoader = new MockClassLoader();

        task.configure(mockClassLoader);
        for (Resource resource : task.getResources())
        {
            mockClassLoader.addResource(resource);
        }
        return mockClassLoader;
    }
}
