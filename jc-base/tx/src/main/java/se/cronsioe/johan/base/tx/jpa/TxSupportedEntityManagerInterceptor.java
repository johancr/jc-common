package se.cronsioe.johan.base.tx.jpa;

import se.cronsioe.johan.base.tx.spi.AbstractTxSupportedInterceptor;

import javax.persistence.EntityManager;


public class TxSupportedEntityManagerInterceptor extends AbstractTxSupportedInterceptor<EntityManager> {

    @Override
    protected boolean hasExpired(EntityManager entityManager) {
        return entityManager == null || !entityManager.isOpen();
    }

    @Override
    protected Class<EntityManager> resourceClass() {
        return EntityManager.class;
    }
}
