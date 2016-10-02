package se.cronsioe.johan.test.junit.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamMatchers {

    public static Matcher<InputStream> streamContains(final String expected) {

        return new TypeSafeMatcher<InputStream>() {

            @Override
            protected boolean matchesSafely(InputStream actual) {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(actual));

                try
                {
                    while ((line = reader.readLine()) != null)
                    {
                        if (line.contains(expected))
                        {
                            return true;
                        }
                    }
                }
                catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }

                return false;
            }

            @Override
            protected void describeMismatchSafely(InputStream item, Description mismatchDescription) {
                mismatchDescription.appendText("but it didn't");
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("inputstream contains \"" + expected + "\"");
            }
        };
    }
}
