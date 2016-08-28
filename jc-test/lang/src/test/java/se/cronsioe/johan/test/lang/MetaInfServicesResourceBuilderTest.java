package se.cronsioe.johan.test.lang;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MetaInfServicesResourceBuilderTest {

    @Test
    public void buildsMetaInfResource() {
        Resource resource = MetaInfServicesResourceBuilder.service(List.class)
                .addImplementation(ArrayList.class)
                .addImplementation(LinkedList.class)
                .build();

        assertThat(resource.getName(), is(List.class.getName()));
        assertThat(resource.getDirectory(), is("META-INF/services"));
        assertThat(resource.getContent(), containsString(ArrayList.class.getName()));
        assertThat(resource.getContent(), containsString(LinkedList.class.getName()));
    }
}
