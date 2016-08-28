package se.cronsioe.johan.base.transaction.jta.stories;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.base.transaction.TransactionListener;
import se.cronsioe.johan.base.transaction.guice.JTAModule;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.transaction.TransactionManager;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JTASupportSteps {

    private EJBContainer container;

    private Provider<Context> contextProvider;
    private Provider<Transaction> transactionProvider;
    private Transaction transaction;
    private TransactionListener listener;

    @Before
    public void setUp() {
        container = EJBContainer.createEJBContainer();

        Injector injector = Guice.createInjector(new JTAModule());
        contextProvider = injector.getProvider(Context.class);
        transactionProvider = injector.getProvider(Transaction.class);
    }

    @Given("^an active transaction$")
    public void an_active_transaction() throws Throwable {
        Context context = contextProvider.get();
        TransactionManager manager = (TransactionManager) context.lookup("java:comp/TransactionManager");
        manager.begin();

        transaction = transactionProvider.get();
        assertThat(transaction.isActive(), is(true));
    }

    @When("^I bind a transaction listener to the transaction$")
    public void i_bind_a_transaction_listener_to_the_transaction() throws Throwable {
        listener = mock(TransactionListener.class);
        transaction.add(listener);
    }

    @When("^the transaction is committed$")
    public void the_transaction_is_committed() throws Throwable {
        Context context = contextProvider.get();
        TransactionManager manager = (TransactionManager) context.lookup("java:comp/TransactionManager");
        manager.commit();
    }

    @Then("^the listener is notified$")
    public void the_listener_is_notified() throws Throwable {
        verify(listener).onCommit();
    }

    @When("^I bind a resource to the transaction$")
    public void i_bind_a_resource_to_the_transaction() throws Throwable {
        transaction.bind(String.class, "hello");
    }

    @Then("^I can lookup the resource while the transaction is active$")
    public void i_can_lookup_the_resource_while_the_transaction_is_active() throws Throwable {
        String resource = transaction.lookup(String.class);

        assertThat("resource was bound", resource, is("hello"));
    }
}
