package se.cronsioe.johan.test.jpa;

import org.junit.After;
import org.junit.Test;
import se.cronsioe.johan.test.jpa.impl.EntityManagerFactoryFactoryImpl;
import se.cronsioe.johan.test.jpa.impl.PersistenceUnitPropertiesProvider;
import se.cronsioe.johan.test.jpa.impl.PersistenceUnitProvider;
import se.cronsioe.johan.test.jpa.spi.EntityManagerFactoryFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EntityManagerFactoryFactoryImplTest {

    private EntityManagerFactory emf;

    @After
    public void tearDown() {
        emf.close();
    }

    @Test
    public void factoryGetsPersistenceUnitFromProvider() {
        PersistenceUnitProvider persistenceUnitProvider = testPersistenceUnitProvider();
        PersistenceUnitPropertiesProvider persistenceUnitPropertiesProvider = new PersistenceUnitPropertiesProvider();
        EntityManagerFactoryFactory emff = new EntityManagerFactoryFactoryImpl(persistenceUnitProvider, persistenceUnitPropertiesProvider);

        emf = emff.create();
        EntityManager entityManager = emf.createEntityManager();

        Map<String, Object> properties = entityManager.getProperties();
        assertThat((String) properties.get("persistence.unit.name"), is("test"));
    }

    private static PersistenceUnitProvider testPersistenceUnitProvider() {
        return new PersistenceUnitProvider() {
            @Override
            public String get() {
                return "test";
            }
        };
    }

    @Test
    public void factoryGetsPropertiesFromProvider() {
        PersistenceUnitProvider persistenceUnitProvider = testPersistenceUnitProvider();
        PersistenceUnitPropertiesProvider persistenceUnitPropertiesProvider = testPersistenceUnitPropertiesProvider();
        EntityManagerFactoryFactory emff = new EntityManagerFactoryFactoryImpl(persistenceUnitProvider, persistenceUnitPropertiesProvider);

        emf = emff.create();
        EntityManager entityManager = emf.createEntityManager();

        Map<String, Object> properties = entityManager.getProperties();
        assertThat((String) properties.get("javax.persistence.jdbc.url"), is("jdbc:h2:mem:tmp"));
        assertThat((String) properties.get("foo"), is("bar"));
    }

    private PersistenceUnitPropertiesProvider testPersistenceUnitPropertiesProvider() {
        return new PersistenceUnitPropertiesProvider() {
            @Override
            public Map<String, String> get() {
                return new HashMap<String, String>() {{
                    put("foo", "bar");
                    put("javax.persistence.jdbc.url", "jdbc:h2:mem:tmp");
                    put("javax.persistence.transactionType", "RESOURCE_LOCAL");
                }};
            }
        };
    }
}
