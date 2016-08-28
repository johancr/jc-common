package se.cronsioe.johan.test.lang;

import java.util.ArrayList;
import java.util.Collection;

public class MetaInfServicesResourceBuilder {

    private Class<?> service;
    private Collection<String> implementations;

    private MetaInfServicesResourceBuilder(Class<?> service) {
        this.service = service;
        this.implementations = new ArrayList<String>();
    }

    public static MetaInfServicesResourceBuilder service(Class<?> service) {
        return new MetaInfServicesResourceBuilder(service);
    }

    public MetaInfServicesResourceBuilder addImplementation(Class<?> implementation) {
        return addImplementation(implementation.getName());
    }

    public MetaInfServicesResourceBuilder addImplementation(String implementation) {
        implementations.add(implementation);
        return this;
    }

    public Resource build() {

        StringBuilder sb = new StringBuilder();
        for (String implementation : implementations)
        {
            sb.append(implementation + "\n");
        }

        return Resource.named(service.getName())
                .inDirectory("META-INF/services")
                .withContent(sb.toString())
                .build();
    }
}
