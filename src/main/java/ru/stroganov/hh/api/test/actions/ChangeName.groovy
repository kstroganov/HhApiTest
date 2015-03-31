package ru.stroganov.hh.api.test.actions

import com.vkharlamov.grooviola.Assertions
import com.vkharlamov.grooviola.BaseTestElement
import com.vkharlamov.grooviola.TestException
import com.vkharlamov.grooviola.TestType
import groovyx.net.http.ContentType
import groovyx.net.http.EncoderRegistry
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.log4j.Level
import ru.stroganov.hh.api.test.TestDataProvider

/**
 * Created by stroganov on 31.03.2015.
 */
class ChangeName extends BaseTestElement {
    private final TestDataProvider dataProvider;
    private final LogIn logIn;
    private final String newUserFirstName;
    private final String newUserLastName;
    private final String newUserMiddleName;

    ChangeName(TestDataProvider dataProvider, LogIn logIn, String newUserFirstName, String newUserLastName, String newUserMiddleName = null) {
        title = "Change User Name";
        testType = TestType.WHEN;
        this.dataProvider = dataProvider;
        this.logIn = logIn;
        this.newUserFirstName = newUserFirstName ?: "";
        this.newUserLastName = newUserLastName ?: "";
        this.newUserMiddleName = newUserMiddleName ?: "";
    }

    @Override
    void run() {
        comment(Level.INFO, "Trying to change user name using URL: $dataProvider.hhApiUrl");
        comment(Level.INFO, "And following user parameters:\nFirst name: $newUserFirstName\nMiddle name: $newUserMiddleName\nLast name: $newUserLastName");
        HTTPBuilder http = new HTTPBuilder(dataProvider.hhApiUrl);
        EncoderRegistry er = new EncoderRegistry();
        er.setCharset('UTF-8');
        http.encoderRegistry = er;
        http.request(Method.POST) {
            String tokenType = logIn.tokenType;
            headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4";
            headers.'Host' = "api.hh.ru";
            headers.'Accept' = "*/*";
            headers.'Authorization' = "$tokenType $logIn.accessToken";
            uri.path = '/me'
            requestContentType = ContentType.URLENC;
            body = [first_name:newUserFirstName, last_name:newUserLastName, middle_name:newUserMiddleName];

            response.success = { resp, reader ->
                sharedData['nameChanged'] = true;
                comment(Level.INFO, "Got response: ${resp.statusLine}");
                comment(Level.INFO, "User name successfully modified.");
            }

            response.failure = { resp ->
                comment(Level.INFO, "Got response: ${resp.statusLine}");
                throw new TestException("Couldn't modify user name. Request failed with status ${resp.status}");
            }
        }
    }
}
