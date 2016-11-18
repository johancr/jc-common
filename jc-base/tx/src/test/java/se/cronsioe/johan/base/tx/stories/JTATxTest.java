package se.cronsioe.johan.base.tx.stories;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:se/cronsioe/johan/base/tx/stories/JTATx.feature")
public class JTATxTest {

}
