package se.cronsioe.johan.test.jpa;

import com.google.inject.Provider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import se.cronsioe.johan.base.jpa.impl.EntityManagerProvider;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.test.jpa.impl.EntityManagerFactoryProvider;
import se.cronsioe.johan.test.jpa.spi.EntityManagerFactoryFactory;
import se.cronsioe.johan.test.transaction.annotation.Transactional;
import se.cronsioe.johan.test.transaction.junit.TransactionRule;
import se.cronsioe.johan.test.transaction.local.LocalTransactionManager;
import se.cronsioe.johan.test.transaction.local.LocalTransactionProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class EntityManagerFactoryProviderTest {

    private final LocalTransactionManager transactionManager = new LocalTransactionManager();
    private final Provider<Transaction> transactionProvider = new LocalTransactionProvider(transactionManager);

    @Rule
    public TransactionRule transactionRule = new TransactionRule(transactionManager);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void providesEntityManagerFactory() {
        EntityManagerFactoryProvider entityManagerFactoryProvider =
                new EntityManagerFactoryProvider(new TestEntityManagerFactoryFactory());

        EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.get();

        assertThat(entityManagerFactory, is(notNullValue()));
    }

    @Test
    public void sameInstanceOnConsecutiveGet() {
        EntityManagerFactoryProvider entityManagerFactoryProvider =
                new EntityManagerFactoryProvider(new TestEntityManagerFactoryFactory());

        EntityManagerFactory emf1 = entityManagerFactoryProvider.get();
        EntityManagerFactory emf2 = entityManagerFactoryProvider.get();

        assertThat(emf1, is(notNullValue()));
        assertThat(emf1, is(emf2));
    }

    @Test
    public void closingFactoryForcesCreationOfNewOne() {
        EntityManagerFactoryProvider entityManagerFactoryProvider =
                new EntityManagerFactoryProvider(new TestEntityManagerFactoryFactory());

        EntityManagerFactory emf1 = entityManagerFactoryProvider.get();
        emf1.close();
        EntityManagerFactory emf2 = entityManagerFactoryProvider.get();

        assertThat(emf1, is(notNullValue()));
        assertThat(emf1, is(not(emf2)));
    }

    @Test
    @Transactional
    public void newFactoryRunsDdlGeneration() {
        Provider<EntityManagerFactory> entityManagerFactoryProvider =
                new EntityManagerFactoryProvider(new TestEntityManagerFactoryFactory());
        EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.get();
        Provider<EntityManager> entityManagerProvider = new EntityManagerProvider(entityManagerFactoryProvider, transactionProvider);
        JPAExecutor executor = new JPAExecutor(entityManagerProvider);

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
        Provider<EntityManagerFactory> entityManagerFactoryProvider = new EntityManagerFactoryProvider(
                new TestEntityManagerFactoryFactory());

        EntityManagerFactory factory = entityManagerFactoryProvider.get();
        EntityManager entityManager = factory.createEntityManager();

        assertThat(entityManager.isOpen(), is(true));
        factory.close();
        assertThat(entityManager.isOpen(), is(false));
    }

    private static class TestEntityManagerFactoryFactory implements EntityManagerFactoryFactory {
        @Override
        public EntityManagerFactory create() {
            return Persistence.createEntityManagerFactory("test", new HashMap() {{
                put("javax.persistence.transactionType", "RESOURCE_LOCAL");
                put("javax.persistence.jdbc.url", "jdbc:h2:mem:" + UUID.randomUUID());
                put("eclipselink.ddl-generation", "drop-and-create-tables");
            }});
        }
    }
}
