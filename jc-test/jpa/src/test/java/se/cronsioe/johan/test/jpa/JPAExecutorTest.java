package se.cronsioe.johan.test.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import se.cronsioe.johan.test.jpa.guice.JPATestModule;
import se.cronsioe.johan.test.junit.GuiceModules;
import se.cronsioe.johan.test.junit.GuiceRunner;
import se.cronsioe.johan.test.transaction.annotation.Transactional;
import se.cronsioe.johan.test.transaction.junit.TransactionRule;

import javax.persistence.EntityManager;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(GuiceRunner.class)
@GuiceModules(JPATestModule.class)
public class JPAExecutorTest {

    @Inject
    @Rule
    public TransactionRule transactionRule;

    @Inject
    private Provider<EntityManager> entityManagerProvider;

    private JPAExecutor executor;

    @Before
    public void setUp() {
        executor = new JPAExecutor(entityManagerProvider);
    }

    @Test
    @Transactional
    public void executeTaskInTransaction() {

        FooEO eo = entityManager().find(FooEO.class, 1L);
        assertThat(eo, is(nullValue()));

        executor.execute(new JPATask<Void>() {
            @Override
            public Void run(EntityManager entityManager) {
                entityManager.persist(new FooEO());
                return null;
            }
        });

        assertThat(entityManager().find(FooEO.class, 1L), is(notNullValue()));
    }

    @Test
    @Transactional
    public void doNotCommitTransactionIfNotTheOneWhoStartedIt() {

        executor.execute(new JPATask<Void>() {
            @Override
            public Void run(EntityManager entityManager) {

                executor.execute(new JPATask<Void>() {
                    @Override
                    public Void run(EntityManager entityManager) {
                        entityManager.persist(new FooEO());
                        return null;
                    }
                });

                assertThat(entityManager.getTransaction().isActive(), is(true));

                return null;
            }
        });
    }

    private EntityManager entityManager() {
        return entityManagerProvider.get();
    }
}
