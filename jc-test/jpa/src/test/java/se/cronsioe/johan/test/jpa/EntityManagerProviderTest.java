package se.cronsioe.johan.test.jpa;

import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import se.cronsioe.johan.test.jpa.guice.JPATestModule;
import se.cronsioe.johan.test.junit.GuiceModules;
import se.cronsioe.johan.test.junit.GuiceRunner;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(GuiceRunner.class)
@GuiceModules(JPATestModule.class)
public class EntityManagerProviderTest {

    @Inject
    private Provider<EntityManager> entityManagerProvider;

    @Inject
    private JPAExecutor executor;

    @Inject
    private Provider<EntityManagerFactory> entityManagerFactoryProvider;

    @Test
    public void getsEntityManager() {
        EntityManager entityManager = entityManagerProvider.get();

        assertThat(entityManager, is(not(nullValue())));
    }

    @Test
    public void ifEntityManagerIsClosedReturnNewOne() {
        EntityManager first = entityManagerProvider.get();
        first.close();

        EntityManager second = entityManagerProvider.get();

        assertThat(first, is(not(second)));
    }
}
