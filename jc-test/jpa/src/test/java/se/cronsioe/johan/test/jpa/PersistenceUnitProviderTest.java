package se.cronsioe.johan.test.jpa;

import org.junit.Test;
import se.cronsioe.johan.test.jpa.impl.PersistenceUnitProvider;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PersistenceUnitProviderTest {

    @Test
    public void providesTheFirstPersistenceUnitFoundInPersistenceXML() {
        PersistenceUnitProvider persistenceUnitProvider = new PersistenceUnitProvider();

        String persistenceUnit = persistenceUnitProvider.get();

        assertThat(persistenceUnit, is("test"));
    }
}
