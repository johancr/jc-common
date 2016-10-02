package se.cronsioe.johan.test.transaction.junit;

import com.google.inject.Inject;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import se.cronsioe.johan.test.transaction.annotation.Transactional;
import se.cronsioe.johan.test.transaction.local.LocalTransactionManager;

public class TransactionRule implements TestRule {

    private final LocalTransactionManager localTransactionManager;

    @Inject
    public TransactionRule(LocalTransactionManager localTransactionManager) {
        this.localTransactionManager = localTransactionManager;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                boolean shouldRunInTransaction = description.getAnnotation(Transactional.class) != null;

                if (shouldRunInTransaction)
                {
                    localTransactionManager.begin();

                    base.evaluate();

                    if (localTransactionManager.getTransaction().isActive())
                    {
                        localTransactionManager.commit();
                    }
                }
                else
                {
                    base.evaluate();
                }
            }
        };
    }
}
