package ru.stroganov.hh.api.test.actions

import com.vkharlamov.grooviola.Assertions
import com.vkharlamov.grooviola.BaseTestElement
import com.vkharlamov.grooviola.TestException
import com.vkharlamov.grooviola.TestType
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import org.apache.log4j.Level
import ru.stroganov.hh.api.test.TestDataProvider

/**
 * Created by stroganov on 30.03.2015.
 */
class Me extends BaseTestElement {
    private final LogIn login;
    private final TestDataProvider dataProvider;
    private Map userInfo;

    Me(TestDataProvider dataProvider, LogIn login) {
        title = "Fetch User Info from $dataProvider.hhApiUrl"
        testType = TestType.WHEN;
        this.dataProvider = dataProvider;
        this.login = login;
    }

    Map getUserInfo() { userInfo }

    @Override
    void run() {
        comment(Level.INFO, "Trying to fetch user info using URL: $dataProvider.hhApiUrl");
        HTTPBuilder http = new HTTPBuilder(dataProvider.hhApiUrl);
        http.request(Method.GET) {
            String tokenType = login.tokenType;
            headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4";
            headers.'Host' = "api.hh.ru";
            headers.'Accept' = "*/*";
            headers.'Authorization' = "$tokenType $login.accessToken";
            uri.path = '/me'

            response.success = { resp, reader ->
                comment(Level.INFO, "Got response: ${resp.statusLine}");
                Assertions.assertEquals(resp.statusLine.statusCode, 200, "Unexpected status code");
                comment(Level.INFO, "User Info successfully received: $reader.");
                userInfo = reader;
            }

            response.failure = { resp ->
                comment(Level.INFO, "Got response: ${resp.statusLine}");
                throw new TestException("Couldn't get User Info. Request failed with status ${resp.status}");
            }
        }
    }
}
