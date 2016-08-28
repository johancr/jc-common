package se.cronsioe.johan.test.jpa.impl;

import com.google.inject.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PersistenceUnitPropertiesProvider implements Provider<Map<String, String>> {

    @Override
    public Map<String, String> get() {
        return new HashMap<String, String>() {{
            put("javax.persistence.jdbc.url", "jdbc:h2:mem:" + UUID.randomUUID());
            put("eclipselink.ddl-generation", "drop-and-create-tables");
            put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        }};
    }
}
