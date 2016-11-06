package se.cronsioe.johan.test.tx.jpa;

import com.google.inject.Provider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

	private final String persistenceUnit;
	
	public EntityManagerFactoryProvider(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}
	
	@Override
	public EntityManagerFactory get() {
		return Persistence.createEntityManagerFactory(persistenceUnit);
	}
}
