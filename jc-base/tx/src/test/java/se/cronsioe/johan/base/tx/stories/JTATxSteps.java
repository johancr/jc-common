package se.cronsioe.johan.base.tx.stories;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.cronsioe.johan.base.tx.jta.JTATxModule;
import se.cronsioe.johan.base.tx.spi.AbstractTxSupportedInterceptor;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.TransactionManager;
import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class JTATxSteps {

    @EJB
    private Caller transactionalCaller;
    private Context context;

    private FooProvider fooProvider;
    private Foo foo;
    private Foo anotherFoo;

    @Before
    public void setUp() throws Exception {
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
        context = new InitialContext();

        Injector injector = Guice.createInjector(new JTATxModule(), new FooModule());
        fooProvider = injector.getInstance(FooProvider.class);
    }

    @Given("^a JTA transaction$")
    public void a_JTA_transaction() throws Throwable {
        TransactionManager manager = (TransactionManager) context.lookup("java:comp/TransactionManager");
        manager.begin();
    }

    @When("^a Foo is requested$")
    public void a_Foo_is_requested() throws Throwable {
        transactionalCaller.call(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                foo = fooProvider.get();
                return null;
            }
        });
    }

    @When("^another Foo is requested$")
    public void another_Foo_is_requested() throws Throwable {
        transactionalCaller.call(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                anotherFoo = fooProvider.get();
                return null;
            }
        });
    }

    @Then("^Foo is the same instance as another Foo$")
    public void foo_is_the_same_instance_as_another_Foo() throws Throwable {
        assertThat(foo, is(sameInstance(anotherFoo)));
    }


    @Then("^the Foo provider was only called once$")
    public void the_Foo_provider_was_only_called_once() throws Throwable {
        assertThat(fooProvider.getTimesCalled(), is(1));
    }

    public interface Caller {
        <V> V call(Callable<V> callable) throws Exception;
    }

    private static class Foo {
    }

    public static class FooProvider implements Provider<Foo> {

        private static int timesCalled = 0;

        @Override
        public Foo get() {
            timesCalled++;
            return new Foo();
        }

        public int getTimesCalled() {
            return timesCalled;
        }
    }

    private static class TxSupportedFooInterceptor extends AbstractTxSupportedInterceptor<Foo> {

        @Override
        protected Class<Foo> resourceClass() {
            return Foo.class;
        }
    }

    private static class FooModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(Foo.class).toProvider(FooProvider.class);

            TxSupportedFooInterceptor interceptor = new TxSupportedFooInterceptor();
            requestInjection(interceptor);
            bindInterceptor(Matchers.subclassesOf(Provider.class), Matchers.returns(Matchers
                    .subclassesOf(Foo.class)), interceptor);
        }
    }

    @Stateless
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public static class TransactionBean implements Caller {

        public <V> V call(Callable<V> callable) throws Exception {
            return callable.call();
        }
    }
}
