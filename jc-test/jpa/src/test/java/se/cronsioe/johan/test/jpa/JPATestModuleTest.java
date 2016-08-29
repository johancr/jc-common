package se.cronsioe.johan.test.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.model.InitializationError;
import se.cronsioe.johan.test.jpa.guice.JPATestModule;
import se.cronsioe.johan.test.junit.GuiceRunner;
import se.cronsioe.johan.test.junit.GuiceModules;
import se.cronsioe.johan.test.transaction.annotation.Transactional;
import se.cronsioe.johan.test.transaction.junit.TransactionRule;

import javax.persistence.EntityManager;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JPATestModuleTest {

    @Test
    public void closeDatabaseAfterEachTest() throws InitializationError {
        GuiceRunner guiceRunner = new GuiceRunner(JPATest.class);
        JUnitCore junitCore = new JUnitCore();

        Result result = junitCore.run(guiceRunner);

        assertThat(result.wasSuccessful(), is(true));
    }

    @GuiceModules(JPATestModule.class)
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    @RunWith(GuiceRunner.class)
    public static class JPATest {

        @Inject
        @Rule
        public TransactionRule transactionRule;

        @Inject
        private Provider<EntityManager> entityManagerProvider;

        @Inject
        private JPAExecutor executor;

        @Test
        @Transactional
        public void a() {
            executor.execute(new JPATask<Void>() {
                @Override
                public Void run(EntityManager entityManager) {
                    entityManager.persist(new FooEO());
                    return null;
                }
            });

            executor.execute(new JPATask<Void>() {
                @Override
                public Void run(EntityManager entityManager) {
                    assertThat(entityManager.find(FooEO.class, 1L), is(notNullValue()));
                    return null;
                }
            });
        }

        @Test
        @Transactional
        public void b() {
            executor.execute(new JPATask<Void>() {
                @Override
                public Void run(EntityManager entityManager) {
                    assertThat(entityManager.find(FooEO.class, 1L), is(nullValue()));
                    return null;
                }
            });
        }
    }
}
