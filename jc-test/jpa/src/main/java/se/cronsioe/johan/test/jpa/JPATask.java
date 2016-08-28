package se.cronsioe.johan.test.jpa;

import javax.persistence.EntityManager;

public interface JPATask<R> {

    R run(EntityManager entityManager);
}
