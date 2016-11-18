package se.cronsioe.johan.test.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.junit.Test;
import org.junit.runner.RunWith;
import se.cronsioe.johan.test.jpa.guice.JPATestModule;
import se.cronsioe.johan.test.jpa.impl.EntityManagerFactoryProvider;
import se.cronsioe.johan.test.jpa.impl.PersistenceUnitPropertiesProvider;
import se.cronsioe.johan.test.jpa.impl.PersistenceUnitProvider;
import se.cronsioe.johan.test.junit.GuiceModules;
import se.cronsioe.johan.test.junit.GuiceRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(GuiceRunner.class)
@GuiceModules(JPATestModule.class)
public class EntityManagerFactoryProviderTest {

    @Inject
    private Provider<EntityManagerFactory> entityManagerFactoryProvider;

    @Inject
    private JPAExecutor executor;

    @Test
    public void providesEntityManagerFactory() {
        EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.get();

        assertThat(entityManagerFactory, is(notNullValue()));
    }

    @Test
    public void sameInstanceOnConsecutiveGet() {
        EntityManagerFactory emf1 = entityManagerFactoryProvider.get();
        EntityManagerFactory emf2 = entityManagerFactoryProvider.get();

        assertThat(emf1, is(notNullValue()));
        assertThat(emf1, is(emf2));
    }

    @Test
    public void closingFactoryForcesCreationOfNewOne() {
        EntityManagerFactory emf1 = entityManagerFactoryProvider.get();
        emf1.close();
        EntityManagerFactory emf2 = entityManagerFactoryProvider.get();

        assertThat(emf1, is(notNullValue()));
        assertThat(emf1, is(not(emf2)));
    }

    @Test
    public void newFactoryRunsDdlGeneration() {
        EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.get();

        long id = persistFoo(executor);
        assertThat(wasSaved(executor, id), is(true));
        entityManagerFactory.close();

        assertThat("tables dropped", wasSaved(executor, id), is(false));
        persistFoo(executor);
        assertThat("tables recreated", wasSaved(executor, id), is(true));
    }

    private long persistFoo(JPAExecutor executor) {
        return executor.execute(new JPATask<Long>() {
            @Override
            public Long run(EntityManager entityManager) {
                FooEO foo = new FooEO();
                entityManager.persist(foo);
                return foo.getId();
            }
        });
    }

    private boolean wasSaved(JPAExecutor executor, final long id) {
        return executor.execute(new JPATask<Boolean>() {
            @Override
            public Boolean run(EntityManager entityManager) {
                return entityManager.find(FooEO.class, id) != null;
            }
        });
    }

    @Test
    public void closingFactoryClosesEntityManager() {
        EntityManagerFactory factory = entityManagerFactoryProvider.get();
        EntityManager entityManager = factory.createEntityManager();

        assertThat(entityManager.isOpen(), is(true));
        factory.close();
        assertThat(entityManager.isOpen(), is(false));
    }

    @Test
    public void factoryGetsPersistenceUnitFromProvider() {
        PersistenceUnitProvider persistenceUnitProvider = testPersistenceUnitProvider();
        PersistenceUnitPropertiesProvider persistenceUnitPropertiesProvider = new PersistenceUnitPropertiesProvider();
        Provider<EntityManagerFactory> entityManagerFactoryProvider = new EntityManagerFactoryProvider(persistenceUnitProvider, persistenceUnitPropertiesProvider);

        EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.get();
        EntityManager entityManager = entityManagerFactory.createEntityManager();

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

        Provider<EntityManagerFactory> entityManagerFactoryProvider = new EntityManagerFactoryProvider(persistenceUnitProvider, persistenceUnitPropertiesProvider);
        EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.get();
        EntityManager entityManager = entityManagerFactory.createEntityManager();

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
