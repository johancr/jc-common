package se.cronsioe.johan.base.bootstrap.stories;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:se/cronsioe/johan/base/bootstrap/stories/Bootstrap.feature"}
)
public class BootstrapTest {
}
