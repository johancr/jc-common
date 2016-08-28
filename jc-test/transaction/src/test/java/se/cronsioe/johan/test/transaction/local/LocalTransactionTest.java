package se.cronsioe.johan.test.transaction.local;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.test.transaction.annotation.Transactional;
import se.cronsioe.johan.test.transaction.guice.LocalTransactionModule;
import se.cronsioe.johan.test.transaction.junit.TransactionRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocalTransactionTest {

    @Test
    public void testsAnnotatedWithTransactionalAreRunInTransaction() {
        JUnitCore core = new JUnitCore();

        Result result = core.run(TestWithTransaction.class);
        System.out.println(result.getFailures());
        assertThat("all tests passed", result.getFailureCount(), is(0));
    }

    public static class TestWithTransaction {

        private Injector injector = Guice.createInjector(new LocalTransactionModule());

        @Rule
        public TransactionRule transactionRule = injector.getInstance(TransactionRule.class);

        @Transactional
        @Test
        public void testTransactional() {
            Transaction transaction = injector.getInstance(Transaction.class);

            assertThat(transaction.isActive(), is(true));
        }
    }
}
