package ru.stroganov.hh.api.test

import com.vkharlamov.grooviola.BaseTestElement
import org.apache.log4j.Level
import ru.stroganov.hh.api.test.actions.ChangeName
import ru.stroganov.hh.api.test.actions.CheckMe
import ru.stroganov.hh.api.test.actions.LogIn
import ru.stroganov.hh.api.test.actions.Me

/**
 * Created by stroganov on 27.03.2015.
 */
class SmokeTest extends BaseTestElement {
    SmokeTest(TestDataProvider dataProvider) {
        LogIn login = new LogIn(dataProvider);
        addChild(login);
        Me me = new Me(dataProvider, login);
        addChild(me);
        addChild(new CheckMe(dataProvider.userInfoExpectedData, me));
        ChangeName changeName = new ChangeName(dataProvider, login, dataProvider.newUserFirstName, dataProvider.newUserLastName);
        addChild(changeName);
        addChild(me);
        addChild(new CheckMe(dataProvider.newUserInfoExpectedData, me));
        Finally("Restore changes made in previouse steps") {
            if (changeName.sharedData['nameChanged']) {
                comment(Level.INFO, "Restore changes...")
                new ChangeName(dataProvider, login, dataProvider.userInfoExpectedData['first_name'],
                        dataProvider.userInfoExpectedData['last_name']).run();
            }
        }
    }
}
