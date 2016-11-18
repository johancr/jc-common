package se.cronsioe.johan.test.tx.local.junit;

import com.google.inject.Inject;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import se.cronsioe.johan.base.tx.TxException;
import se.cronsioe.johan.base.tx.TxScope;

public class TransactionRule implements TestRule {

    private final TxScope txScope;

    @Inject
    public TransactionRule(TxScope txScope) {
        this.txScope = txScope;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                boolean shouldRunInTransaction = description.getAnnotation(Transactional.class) != null;

                if (shouldRunInTransaction)
                {
                    txScope.begin();

                    base.evaluate();

                    try
                    {
                        txScope.rollback();
                    }
                    catch (TxException ex)
                    {
                        // do nothing
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
