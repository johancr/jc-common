package se.cronsioe.johan.test.tx.local.jpa;

import se.cronsioe.johan.base.tx.Tx;
import se.cronsioe.johan.base.tx.TxListener;
import se.cronsioe.johan.base.tx.spi.AbstractTxSupportedInterceptor;

import javax.persistence.EntityManager;


public class LocalTxSupportedEntityManagerInterceptor extends AbstractTxSupportedInterceptor<EntityManager> {

    @Override
    protected Class<EntityManager> resourceClass() {
        return EntityManager.class;
    }

    @Override
    protected void postCreate(final EntityManager entityManager, Tx tx) {
        entityManager.getTransaction().begin();

        tx.add(new TxListener() {
            @Override
            public void onCommit() {
                entityManager.getTransaction().commit();
            }

            @Override
            public void onRollback() {
                entityManager.getTransaction().rollback();
            }

            @Override
            public void onEnd() {
                entityManager.close();
            }
        });
    }
}
