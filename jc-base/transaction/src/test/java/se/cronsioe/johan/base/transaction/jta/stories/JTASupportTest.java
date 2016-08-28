package se.cronsioe.johan.base.transaction.jta.stories;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:se/cronsioe/johan/base/transaction/jta/stories/JTASupport.feature"}
)
public class JTASupportTest {
}
