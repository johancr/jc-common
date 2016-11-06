package se.cronsioe.johan.test.tx.jpa;

import javax.persistence.*;

@Entity(name = "Foo")
public class FooEO
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId()
    {
        return id;
    }
}
