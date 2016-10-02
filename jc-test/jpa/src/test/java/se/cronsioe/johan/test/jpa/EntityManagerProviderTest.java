package se.cronsioe.johan.test.jpa;

import com.google.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.test.jpa.guice.JPATestModule;
import se.cronsioe.johan.test.junit.GuiceModules;
import se.cronsioe.johan.test.junit.GuiceRunner;
import se.cronsioe.johan.test.transaction.annotation.Transactional;
import se.cronsioe.johan.test.transaction.junit.TransactionRule;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(GuiceRunner.class)
@GuiceModules(JPATestModule.class)
public class EntityManagerProviderTest {

    @Rule
    @Inject
    public TransactionRule transactionRule;

    @Inject
    private Provider<EntityManager> entityManagerProvider;

    @Inject
    private JPAExecutor executor;

    @Inject
    private Provider<Transaction> transactionProvider;

    @Inject
    private Provider<EntityManagerFactory> entityManagerFactoryProvider;

    @Test
    @Transactional
    public void getsEntityManager() {
        EntityManager entityManager = entityManagerProvider.get();

        FooEO fooEO = new FooEO();
        entityManager.persist(fooEO);

        assertThat(exists(fooEO.getId()), is(true));
    }

    private boolean exists(final Long id) {
        return executor.execute(new JPATask<Boolean>() {
            @Override
            public Boolean run(EntityManager entityManager) {
                return entityManager.find(FooEO.class, id) != null;
            }
        });
    }

    @Test
    @Transactional
    public void returnsNewProxyAlthoughTheEntityManagerIsTheSame() {
        EntityManager first = entityManagerProvider.get();
        EntityManager second = entityManagerProvider.get();

        assertThat(first, is(not(second)));
    }

    @Test
    @Transactional
    public void ifEntityManagerIsClosedReturnNewOne() {
        EntityManager first = entityManagerProvider.get();
        first.close();

        EntityManager second = entityManagerProvider.get();

        assertThat(first, is(not(second)));
    }

    @Test
    @Transactional
    public void entityManagerIsAProxy() {
        EntityManager entityManager = entityManagerProvider.get();
        assertThat(entityManager.isOpen(), is(true));
        entityManager.close();
        assertThat(entityManager.isOpen(), is(true));
    }

    @Test
    @Transactional
    public void doNotReturnNullIfEntityManagerAlreadyBoundToTransaction() {
        EntityManager entityManager = entityManagerFactoryProvider.get().createEntityManager();
        Transaction transaction = transactionProvider.get();
        transaction.bind(EntityManager.class, entityManager);

        EntityManager fromProvider = entityManagerProvider.get();

        assertThat(fromProvider, is(not(nullValue())));
    }

}
