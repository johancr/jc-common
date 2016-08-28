package se.cronsioe.johan.base.func.stories;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:se/cronsioe/johan/base/func/stories/Func.feature"}
)
public class FuncTest {
}
