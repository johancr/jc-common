package se.cronsioe.johan.test.tx.scope;

import com.google.inject.Inject;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import se.cronsioe.johan.base.tx.Tx;
import se.cronsioe.johan.base.tx.TxException;
import se.cronsioe.johan.base.tx.TxScope;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@ScenarioScoped
public class TxScopeSteps
{
    @Inject
    private TxScope txScope;

    private Tx begunTx;
    private Tx currentTx;
    private Throwable exception;

    @After
    public void tearDown()
    {
        try
        {
            txScope.rollback();
        }
        catch (TxException ex)
        {
            // do nothing
        }
    }

    @Given("^a transaction scope$")
    public void a_transaction_scope()
    {
        assertThat("transaction scope", txScope, is(not(nullValue())));
    }

    @When("^the transaction is begun")
    public void the_transaction_is_begun()
    {
        try
        {
            begunTx = txScope.begin();
        }
        catch (Throwable ex)
        {
            exception = ex;
        }
    }

    @Then("^a transaction is provided$")
    public void a_transaction_is_provided()
    {
        assertThat("transaction", begunTx, is(not(nullValue())));
    }

    @Given("^a transaction scope with a started transaction$")
    public void a_transaction_scope_with_a_started_transaction()
    {
        a_transaction_scope();
        the_transaction_is_begun();
        a_transaction_is_provided();
    }

    @When("^I get a transaction$")
    public void i_get_a_transaction()
    {
        try
        {
            currentTx = txScope.get();
        }
        catch (Throwable ex)
        {
            exception = ex;
        }
    }

    @Then("^the transaction is gotten")
    public void the_transaction_is_gotten()
    {
        assertThat("transaction", currentTx, is(sameInstance(begunTx)));
    }

    @Then("^an exception is thrown$")
    public void an_exception_is_thrown()
    {
        assertThat("thrown exception", exception, is(instanceOf(TxException.class)));
    }

    @Then("^no exception is thrown$")
    public void no_exception_is_thrown()
    {
        assertThat("no exception thrown", exception, is(nullValue()));
    }
}
