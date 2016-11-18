package se.cronsioe.johan.test.jpa.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import org.aopalliance.intercept.MethodInterceptor;
import se.cronsioe.johan.base.jpa.impl.EntityManagerProvider;
import se.cronsioe.johan.test.jpa.impl.EntityManagerFactoryProvider;
import se.cronsioe.johan.test.junit.BeforeAndAfterTask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.regex.Matcher;

public class JPATestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class);
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);

        MethodInterceptor interceptor = new ThreadBoundEntityManagerInterceptor();
        requestInjection(interceptor);
        bindInterceptor(Matchers.subclassesOf(Provider.class),
                Matchers.returns(Matchers.subclassesOf(EntityManager.class)),
                interceptor);

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
