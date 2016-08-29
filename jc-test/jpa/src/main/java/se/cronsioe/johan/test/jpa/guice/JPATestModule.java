package se.cronsioe.johan.test.jpa.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;
import org.junit.runners.model.Statement;
import se.cronsioe.johan.base.jpa.impl.EntityManagerProvider;
import se.cronsioe.johan.test.jpa.impl.EntityManagerFactoryProvider;
import se.cronsioe.johan.test.transaction.guice.LocalTransactionModule;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JPATestModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new LocalTransactionModule());

        bind(EntityManager.class).toProvider(EntityManagerProvider.class);
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);

        Multibinder<Statement> statements = Multibinder.newSetBinder(binder(), Statement.class);
        statements.addBinding().to(CloseDatabaseStatement.class);
    }

    private static class CloseDatabaseStatement extends Statement {

        @Inject
        private Provider<EntityManagerFactory> entityManagerFactoryProvider;

        @Override
        public void evaluate() throws Throwable {
            entityManagerFactoryProvider.get().close();
        }
    }
}
