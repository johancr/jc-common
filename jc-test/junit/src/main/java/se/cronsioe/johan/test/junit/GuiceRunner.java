package se.cronsioe.johan.test.junit;

import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Modules;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class GuiceRunner extends BlockJUnit4ClassRunner {

    private final Injector injector;

    public GuiceRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.injector = createInjector(klass);
    }

    private Injector createInjector(Class<?> klass) {
        Collection<Module> modules = getModules(klass);
        return Guice.createInjector(
                Modules.override(new DefaultBindingsModule()).with(modules));
    }

    private Collection<Module> getModules(Class<?> klass) {
        Collection<Module> modules = new ArrayList<Module>();

        if (klass.isAnnotationPresent(GuiceModules.class))
        {
            GuiceModules guiceModules = klass.getAnnotation(GuiceModules.class);
            for (Class module : guiceModules.value())
            {
                try
                {
                    modules.add((Module) module.newInstance());
                }
                catch (Exception ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        }
        return modules;
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        injector.injectMembers(test);
        return test;
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, final Statement statement) {
        final Set<Statement> statements = injector.getInstance(new Key<Set<Statement>>() {
        });
        final Statement befores = super.withBefores(method, target, statement);

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                for (Statement statement : statements)
                {
                    statement.evaluate();
                }
                befores.evaluate();
            }
        };
    }

    private static class DefaultBindingsModule extends AbstractModule {
        @Override
        protected void configure() {
            Multibinder.newSetBinder(binder(), Statement.class);
        }
    }
}
