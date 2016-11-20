package se.cronsioe.johan.test.tx.foo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.cronsioe.johan.base.bootstrap.Bootstrap;
import se.cronsioe.johan.base.tx.Tx;
import se.cronsioe.johan.base.tx.TxScope;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class FooSteps {

    @Inject
    private TxScope txScope;

    @Inject
    private Provider<FooSession> sessionProvider;

    private FooSession session;
    private FooSession anotherSession;

    @Before
    public void setUp() {
        Bootstrap.inject(this);
    }

    @Given("^a transaction$")
    public void a_transaction() {
        Tx tx = txScope.begin();

        assertThat("tx", tx, is(not(nullValue())));
    }

    @When("^I request a session$")
    public void i_request_a_session() {
        session = sessionProvider.get();

        assertThat("session", session, is(not(nullValue())));
    }

    @When("^the session is open$")
    public void the_session_is_open() {
        assertThat("session is open", session.isClosed(), is(false));
    }

    @When("^transaction is committed$")
    public void transaction_is_committed() {
        txScope.commit();
    }

    @Then("^the session is closed$")
    public void the_session_is_closed() {
        assertThat("session is closed", session.isClosed(), is(true));
    }

    @When("^I close the session$")
    public void i_close_the_session() throws Throwable {
        session.close();
    }

    @When("^I request another session$")
    public void i_request_another_session() {
        anotherSession = sessionProvider.get();

        assertThat("another session", anotherSession, is(not(nullValue())));
    }

    @Then("^I get the same session$")
    public void i_get_the_same_session() {
        assertThat("same session", anotherSession, is(sameInstance(session)));
    }
}
