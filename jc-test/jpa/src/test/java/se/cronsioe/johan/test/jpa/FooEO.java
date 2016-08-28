package se.cronsioe.johan.test.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FooEO extends Foo {

    public static FooEO toEO(Foo foo) {
        return new FooEO();
    }

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }
}
