package se.cronsioe.johan.base.func;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FlatMapperTest {

    @Test
    public void mapAndFlatten() {
        Collection<Box> boxes = Arrays.asList(new Box("a1,a2"), new Box("b1,b2"));

        Collection<String> content = FlatMapper.flatMap(boxes).using(new Function1<Collection<String>, Box>() {
            @Override
            public Collection<String> apply(Box box) {
                return Arrays.asList(box.getContent().split(","));
            }
        });

        assertThat(content.size(), is(4));
        Iterator<String> iter = content.iterator();
        assertThat(iter.next(), is("a1"));
        assertThat(iter.next(), is("a2"));
        assertThat(iter.next(), is("b1"));
        assertThat(iter.next(), is("b2"));
    }

    private static class Box {

        private final String content;

        public Box(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}
