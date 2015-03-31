package ru.stroganov.hh.api.test.actions

import com.vkharlamov.grooviola.BaseTestElement
import com.vkharlamov.grooviola.TestException
import com.vkharlamov.grooviola.TestType
import org.apache.log4j.Level
import ru.stroganov.hh.api.test.TestDataProvider

/**
 * Created by stroganov on 31.03.2015.
 */
class CheckMe extends BaseTestElement {
    private final Me me;
    private final Map expectedUserInfo;

    CheckMe(Map expectedUserInfo, Me me) {
        title = "Check User Info";
        testType = TestType.THEN;
        this.me = me;
        this.expectedUserInfo = expectedUserInfo;
    }

    @Override
    void run() {
        comment(Level.INFO, "Check User Info... Expected data: $expectedUserInfo");
        Map inconsistent = checkUserInfo(expectedUserInfo, me.getUserInfo());
        if (inconsistent.size() != 0)
            throw new TestException("Unexpected User Info: $inconsistent");
        comment(Level.INFO, "Check User Info succeeded.");
    }

    private Map checkUserInfo(Map expected, Map actual) {
        Map inconsistent = [:];
        expected.each {
            if (!actual.containsKey(it.key))
                inconsistent[it.key] = "Record is not present in actual result";
            else if (!it.value.equals(actual[it.key]))
                inconsistent[it.key] = "Expected: ${it.value}; Actual: ${actual[it.key]}"
        }
        return inconsistent;
    }
}
