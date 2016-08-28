package se.cronsioe.johan.test.jpa.eclipselink;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class EclipseLinkTest {

    @Test
    public void cachesFactoryIfSpecificPropertiesAreNotUnique() {
        String uniqueUrl = generateUniqueUrl();
        Map<String, String> configuration = createConfiguration(uniqueUrl);
        EntityManager entityManager = createEntityManager(configuration);
        assertProperties(uniqueUrl, entityManager);

        configuration.put("foo", "bar");
        entityManager = createEntityManager(configuration);

        Map<String, Object> properties = entityManager.getProperties();
        assertThat((String) properties.get("javax.persistence.jdbc.url"), is(uniqueUrl));
        assertThat(properties.get("foo"), is(nullValue()));
    }

    private String generateUniqueUrl() {
        return "jdbc:h2:mem:" + UUID.randomUUID();
    }

    private Map<String, String> createConfiguration(String url) {
        Map<String, String> configuration = new HashMap<String, String>();
        configuration.put("javax.persistence.jdbc.url", url);
        configuration.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        return configuration;
    }

    private EntityManager createEntityManager(Map<String, String> configuration) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test", configuration);
        return emf.createEntityManager();
    }

    private void assertProperties(String UNIQUE_DATABASE, EntityManager entityManager) {
        Map<String, Object> properties = entityManager.getProperties();
        assertThat((String) properties.get("javax.persistence.jdbc.url"), is(UNIQUE_DATABASE));
        assertThat(properties.get("foo"), is(nullValue()));
    }

    @Test
    public void doesNotCacheFactoryIfUrlChanges() {

        String uniqueUrl = generateUniqueUrl();
        Map<String, String> configuration = createConfiguration(uniqueUrl);
        EntityManager entityManager = createEntityManager(configuration);

        assertProperties(uniqueUrl, entityManager);

        String anotherUrl = generateUniqueUrl();
        configuration.put("javax.persistence.jdbc.url", anotherUrl);
        configuration.put("foo", "bar");
        entityManager = createEntityManager(configuration);

        Map<String, Object> properties = entityManager.getProperties();
        assertThat((String) properties.get("javax.persistence.jdbc.url"), is(anotherUrl));
        assertThat((String) properties.get("foo"), is("bar"));
    }
}
