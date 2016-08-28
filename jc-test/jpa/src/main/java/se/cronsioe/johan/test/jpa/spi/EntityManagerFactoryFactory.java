package se.cronsioe.johan.test.jpa.spi;

import javax.persistence.EntityManagerFactory;

public interface EntityManagerFactoryFactory {

    EntityManagerFactory create();
}
