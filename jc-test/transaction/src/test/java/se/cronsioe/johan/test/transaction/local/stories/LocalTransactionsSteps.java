package se.cronsioe.johan.test.transaction.local.stories;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.base.transaction.TransactionListener;
import se.cronsioe.johan.test.transaction.guice.LocalTransactionModule;
import se.cronsioe.johan.test.transaction.local.LocalTransactionManager;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LocalTransactionsSteps {

    private LocalTransactionManager transactionManager;
    private Provider<Transaction> transactionProvider;
    private Transaction transaction;
    private TransactionListener listener;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new LocalTransactionModule());
        transactionManager = injector.getInstance(LocalTransactionManager.class);
        transactionProvider = injector.getProvider(Transaction.class);
    }

    @Given("^an active transaction$")
    public void an_active_transaction() throws Throwable {
        transactionManager.begin();

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
        transactionManager.commit();
    }

    @Then("^the listener is notified$")
    public void the_listener_is_notified() throws Throwable {
        verify(listener).onCommit();
    }
}
