package se.cronsioe.johan.test.jpa.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;
import se.cronsioe.johan.base.jpa.impl.EntityManagerProvider;
import se.cronsioe.johan.test.jpa.impl.EntityManagerFactoryProvider;
import se.cronsioe.johan.test.junit.BeforeAndAfterTask;
import se.cronsioe.johan.test.transaction.guice.LocalTransactionModule;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JPATestModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new LocalTransactionModule());

        bind(EntityManager.class).toProvider(EntityManagerProvider.class);
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);

        Multibinder<BeforeAndAfterTask> tasks = Multibinder.newSetBinder(binder(), BeforeAndAfterTask.class);
        tasks.addBinding().to(CloseDatabaseTask.class);
    }

    private static class CloseDatabaseTask implements BeforeAndAfterTask {

        @Inject
        private Provider<EntityManagerFactory> entityManagerFactoryProvider;

        @Override
        public void before() {
            // do nothing
        }

        @Override
        public void after() {
            entityManagerFactoryProvider.get().close();
        }
    }
}
