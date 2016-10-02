package se.cronsioe.johan.test.jpa;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import se.cronsioe.johan.test.jpa.guice.JPATestModule;
import se.cronsioe.johan.test.junit.GuiceModules;
import se.cronsioe.johan.test.junit.GuiceRunner;
import se.cronsioe.johan.test.transaction.annotation.Transactional;
import se.cronsioe.johan.test.transaction.junit.TransactionRule;

import javax.persistence.EntityManager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(GuiceRunner.class)
@GuiceModules(JPATestModule.class)
public class JPATestUtilsTest {

    @Inject
    @Rule
    public TransactionRule transactionRule;

    @Inject
    private FooDAO fooDAO;

    @Inject
    private Provider<EntityManager> entityManagerProvider;

    @Inject
    private JPAExecutor executor;

    @Test
    @Transactional
    public void createsTransactionalProxy() {
        fooDAO = JPATestUtils.makeTransactional(fooDAO, entityManagerProvider);

        fooDAO.save(new Foo());

        assertThat(wasSaved(), is(true));
    }

    @Test
    @Transactional
    public void joinOngoingTransaction() {
        fooDAO = JPATestUtils.makeTransactional(fooDAO, entityManagerProvider);

        executor.execute(new JPATask<Void>() {
            @Override
            public Void run(EntityManager entityManager) {
                fooDAO.save(new Foo());
                assertThat(entityManager.getTransaction().isActive(), is(true));
                return null;
            }
        });

        assertThat(wasSaved(), is(true));
    }

    private boolean wasSaved() {
        return executor.execute(new JPATask<Boolean>() {
            @Override
            public Boolean run(EntityManager entityManager) {
                return entityManager.createQuery("select f from FooEO f")
                        .getResultList().size() > 0;
            }
        });
    }

    @ImplementedBy(FooDAOImpl.class)
    private interface FooDAO {
        void save(Foo foo);
    }

    private static class FooDAOImpl implements FooDAO {

        private final Provider<EntityManager> entityManagerProvider;

        @Inject
        public FooDAOImpl(Provider<EntityManager> entityManagerProvider) {
            this.entityManagerProvider = entityManagerProvider;
        }

        @Override
        public void save(Foo foo) {
            entityManager().persist(FooEO.toEO(foo));
        }

        private EntityManager entityManager() {
            return entityManagerProvider.get();
        }
    }
}
