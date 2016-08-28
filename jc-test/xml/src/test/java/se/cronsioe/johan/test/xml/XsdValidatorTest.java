package se.cronsioe.johan.test.xml;

import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class XsdValidatorTest {

    @Test
    public void validateXsl() {
        InputStream xml = XsdValidatorTest.class.getResourceAsStream("/xml/test.xml");

        assertThat(XsdValidator.validate(xml), is(true));
    }
}
