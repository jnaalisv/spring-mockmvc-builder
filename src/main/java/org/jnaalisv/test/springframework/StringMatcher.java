package org.jnaalisv.test.springframework;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class StringMatcher extends BaseMatcher<String> {

    private final String pattern;

    public StringMatcher(final String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(Object item) {
        if (item == null) {
            return false;
        }
        if (item instanceof String) {
            String stringItem = (String) item;
            return stringItem.matches(pattern);
        }

        return false;
    }

    @Override
    public void describeTo(Description description) {

    }
}
