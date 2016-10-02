package se.cronsioe.johan.test.transaction.local;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import se.cronsioe.johan.base.transaction.Transaction;
import se.cronsioe.johan.test.junit.GuiceModules;
import se.cronsioe.johan.test.junit.GuiceRunner;
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
        assertThat("all tests passed", result.getFailureCount(), is(0));
    }

    @RunWith(GuiceRunner.class)
    @GuiceModules(LocalTransactionModule.class)
    public static class TestWithTransaction {

        @Rule
        @Inject
        public TransactionRule transactionRule;

        @Inject
        private Provider<Transaction> transactionProvider;

        @Transactional
        @Test
        public void testTransactional() {
            Transaction transaction = transactionProvider.get();

            assertThat(transaction.isActive(), is(true));
        }
    }
}
