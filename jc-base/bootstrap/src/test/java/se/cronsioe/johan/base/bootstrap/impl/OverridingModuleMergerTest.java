package se.cronsioe.johan.base.bootstrap.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import org.junit.Test;
import se.cronsioe.johan.base.bootstrap.spi.ModuleMerger;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OverridingModuleMergerTest {

    @Test
    public void firstModuleIsOverriddenBySecondModule() {
        ModuleMerger moduleMerger = new OverridingModuleMerger();

        Module merged = moduleMerger.merge(Arrays.asList(first(), second()));

        String instance = Guice.createInjector(merged).getInstance(String.class);
        assertThat(instance, is("second"));
    }

    private Module first() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class).toInstance("first");
            }
        };
    }

    private Module second() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class).toInstance("second");
            }
        };
    }
}
