package se.cronsioe.johan.test.jpa.stories;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.cronsioe.johan.base.jpa.guice.JPAModule;
import se.cronsioe.johan.base.transaction.guice.JTAModule;
import se.cronsioe.johan.test.jpa.FooEO;
import se.cronsioe.johan.test.jpa.guice.JPATestModule;
import se.cronsioe.johan.test.transaction.local.LocalTransactionManager;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;
import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


public class UniformBehaviourSteps {

    @EJB
    private Caller transactionalCaller;

    private Injector injector;
    private Provider<EntityManager> entityManagerProvider;

    @Given("^an EntityManager in application container$")
    public void an_EntityManager_in_application_container() throws Throwable {
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
        injector = Guice.createInjector(new JTAModule(), new JPAModule("uniform"));

        Context context = injector.getInstance(Context.class);
        TransactionManager manager = (TransactionManager) context.lookup("java:comp/TransactionManager");
        manager.begin();

        entityManagerProvider = injector.getProvider(EntityManager.class);
    }

    @Given("^an EntityManager in unit test$")
    public void an_EntityManager_in_unit_test() throws Throwable {
        injector = Guice.createInjector(new JPATestModule());

        LocalTransactionManager transactionManager = injector.getInstance(LocalTransactionManager.class);
        transactionManager.begin();

        entityManagerProvider = injector.getProvider(EntityManager.class);
    }

    @When("^an entity is persisted in application container$")
    public void an_entity_is_persisted_in_application_container() throws Throwable {

        transactionalCaller.call(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                persistEntity();
                return null;
            }
        });
    }

    private void persistEntity() {
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(new FooEO());
    }

    @When("^an entity is persisted in unit test$")
    public void an_entity_is_persisted_in_unit_test() {
        persistEntity();
    }

    @When("^the transaction is committed in application container$")
    public void the_transaction_is_committed_in_application_container() throws Throwable {
        Context context = injector.getInstance(Context.class);
        TransactionManager manager = (TransactionManager) context.lookup("java:comp/TransactionManager");
        manager.commit();
    }

    @When("^the transaction is committed in unit test$")
    public void the_transaction_is_committed_in_unit_test() throws Throwable {
        LocalTransactionManager transactionManager = injector.getInstance(LocalTransactionManager.class);
        transactionManager.commit();
    }

    @Then("^the entity has been persisted in application container$")
    public void the_entity_has_been_persisted_in_application_container() throws Throwable {
        transactionalCaller.call(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                assertEntityPersisted();
                return null;
            }
        });
    }

    private void assertEntityPersisted() {
        EntityManager entityManager = entityManagerProvider.get();
        FooEO eo = entityManager.find(FooEO.class, 1L);
        assertThat(eo, is(notNullValue()));
    }

    @Then("^the entity has been persisted in unit test$")
    public void the_entity_has_been_persisted_in_unit_test() throws Throwable {
        LocalTransactionManager transactionManager = injector.getInstance(LocalTransactionManager.class);
        transactionManager.begin();

        assertEntityPersisted();
    }

    public interface Caller {
        <V> V call(Callable<V> callable) throws Exception;
    }

    @Stateless
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public static class TransactionBean implements Caller {

        public <V> V call(Callable<V> callable) throws Exception {
            return callable.call();
        }
    }
}
