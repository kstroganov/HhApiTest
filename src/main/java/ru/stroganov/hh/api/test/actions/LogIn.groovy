package ru.stroganov.hh.api.test.actions

import com.vkharlamov.grooviola.Assertions
import com.vkharlamov.grooviola.BaseTestElement
import com.vkharlamov.grooviola.TestException
import com.vkharlamov.grooviola.TestType
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.log4j.Level
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.NoSuchElementException
import ru.stroganov.hh.api.test.TestDataProvider

import java.util.concurrent.TimeUnit

/**
 * Created by stroganov on 27.03.2015.
 */
class LogIn extends BaseTestElement {
    private final TestDataProvider dataProvider;

    private String accessToken;
    private String refreshToken;
    private int expires;
    private String tokenType;

    LogIn(TestDataProvider dataProvider) {
        title = "Login into hh.ru to get access_token"
        testType = TestType.WHEN;
        this.dataProvider = dataProvider;
    }

    String getAccessToken() { accessToken }
    String getRefreshToken() { refreshToken }
    int getExpires() { expires }
    String getTokenType() { tokenType[0].toUpperCase() + tokenType.substring(1) }

    @Override
    void run() {
        comment(Level.INFO, "Initializing WebDriver");
        WebDriver driver = new HtmlUnitDriver();
        String auth_code;
        try {
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            String url = dataProvider.authBaseURL + "?response_type=code&client_id=" + dataProvider.clientId;
            comment(Level.INFO, "Trying to get URL: ${url}");
            driver.get(url);
            comment(Level.INFO, "Trying to fill LogIn form with the following credantials: Login: $dataProvider.login; Password: $dataProvider.password");
            List<WebElement> loginInputs = driver.findElements(By.className("login-input"));
            if (loginInputs.size() != 2)
                throw new TestException("Unexpected web page contents. Couldn't find input controls.")
            try {
                loginInputs[0].findElement(By.tagName("input")).sendKeys(dataProvider.login);
                loginInputs[1].findElement(By.tagName("input")).sendKeys(dataProvider.password);
                WebElement e = driver.findElement(By.xpath(".//div[@class='account-form-actions']/input"));
                e.click();
            } catch (NoSuchElementException e) {
                throw new TestException("Unexpected web page contents. Couldn't find web control.", e);
            }
            comment(Level.INFO, "User credentials successfully send using web form.");
            String urlWithCode = driver.currentUrl;
            int index = urlWithCode.indexOf("?code=");
            comment(Level.INFO, "Redirected URL: $urlWithCode");
            Assertions.assertNotEquals(index, -1, "Couldn't find authorization_code in redirected URL");
            auth_code = urlWithCode.substring(index + 6);
            comment(Level.INFO, "authorization_code: $auth_code");
        } finally {
            driver.quit();
        }

        comment(Level.INFO, "Trying to get access_token using URL: $dataProvider.accessTokenUrl");
        HTTPBuilder http = new HTTPBuilder(dataProvider.accessTokenUrl);
        http.request(Method.POST) {
            headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4";
            requestContentType = ContentType.URLENC;
            body = [grant_type:'authorization_code', client_id:dataProvider.clientId, client_secret:dataProvider.clientSecret, code:auth_code];

            response.success = { resp, reader ->
                comment(Level.INFO, "Got response: ${resp.statusLine}");
                Assertions.assertEquals(resp.statusLine.statusCode, 200, "Unexpected status code");
                comment(Level.INFO, "access_token successfully received: $reader.");
                accessToken = reader.access_token;
                refreshToken = reader.refresh_token;
                expires = reader.expires_in;
                tokenType = reader.token_type;
            }

            response.failure = { resp ->
                comment(Level.INFO, "Got response: ${resp.statusLine}");
                throw new TestException("Couldn't get access_token. Request failed with status ${resp.status}");
            }
        }
    }
}
