package se.cronsioe.johan.test.lang;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResourcePathResolverTest {

    @Test
    public void resolvePath() {
        ResourcePathResolver resourcePathResolver = new ResourcePathResolver();
        Resource resource = Resource.named("test.txt").inDirectory("directory").build();

        String path = resourcePathResolver.resolve(resource);

        assertThat(path, is("directory/test.txt"));
    }

}
