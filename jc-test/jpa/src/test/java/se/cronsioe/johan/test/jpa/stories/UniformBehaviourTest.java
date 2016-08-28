package se.cronsioe.johan.test.jpa.stories;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:se/cronsioe/johan/test/jpa/stories/UniformBehaviour.feature")
public class UniformBehaviourTest {

}
