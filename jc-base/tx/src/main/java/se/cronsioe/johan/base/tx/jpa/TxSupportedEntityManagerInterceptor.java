package se.cronsioe.johan.base.tx.jpa;

import se.cronsioe.johan.base.tx.spi.AbstractTxSupportedInterceptor;

import javax.persistence.EntityManager;


public class TxSupportedEntityManagerInterceptor extends AbstractTxSupportedInterceptor<EntityManager> {

    @Override
    protected Class<EntityManager> resourceClass() {
        return EntityManager.class;
    }
}
