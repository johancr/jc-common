package se.cronsioe.johan.test.tx.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import se.cronsioe.johan.base.tx.Tx;
import se.cronsioe.johan.base.tx.TxScope;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@ScenarioScoped
public class JPASteps {

    @Inject
    private TxScope txScope;

    @Inject
    private Provider<EntityManager> sessionProvider;

    @Inject
    private EntityManagerFactory entityManagerFactory;

    private Tx tx;
    private EntityManager session;
    private EntityManager anotherSession;
    private FooEO entity;

    @Given("^a transaction$")
    public void a_transaction() {
        tx = txScope.begin();

        assertThat("tx", tx, is(not(nullValue())));
    }

    @When("^I request a session$")
    public void i_request_a_session() {
        session = sessionProvider.get();

        assertThat("session", session, is(not(nullValue())));
    }

    @When("^the session is open$")
    public void the_session_is_open() {
        assertThat("session is open", session.isOpen(), is(true));
    }

    @When("^transaction is committed$")
    public void transaction_is_commited() {
        txScope.commit();
    }

    @When("^transaction is rolled back$")
    public void transaction_is_rolled_back() {
        txScope.rollback();
    }

    @Then("^the session is closed$")
    public void the_session_is_closed() {
        assertThat("session is closed", session.isOpen(), is(false));
    }

    @When("^I request another session$")
    public void i_request_another_session() {
        anotherSession = sessionProvider.get();

        assertThat("another ession", anotherSession, is(not(nullValue())));
    }

    @Then("^I get the same session$")
    public void i_get_the_same_session() {
        assertThat("same session", anotherSession, is(sameInstance(session)));
    }

    @When("^I persist an object in the session")
    public void i_persist_an_object_in_the_session() {
        entity = new FooEO();
        session.persist(entity);
    }

    @Then("^the object is persisted in the database$")
    public void the_object_is_persisted_in_the_database() {
        assertThat("object is given an id", entity.getId(), is(not(nullValue())));

        FooEO result = getEntityFromDatabase();

        assertThat("entity exists in database", result, is(not(nullValue())));
    }

    private FooEO getEntityFromDatabase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(FooEO.class, entity.getId());
    }

    @Then("^the object is not persisted in database$")
    public void the_object_is_not_persisted_in_database() {
        assertThat("object is not given id", entity.getId(), is(not(nullValue())));

        FooEO result = getEntityFromDatabase();

        assertThat("entity not in database", result, is(nullValue()));
    }
}
