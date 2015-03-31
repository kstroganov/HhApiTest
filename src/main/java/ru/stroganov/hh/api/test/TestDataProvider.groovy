package ru.stroganov.hh.api.test

/**
 * Created by stroganov on 27.03.2015.
 */
class TestDataProvider {
    private final Map data;

    TestDataProvider(Map data) {
        this.data = data;
    }

    String getClientId() { data['clientId'] }
    String getClientSecret() { data['clientSecret'] }
    String getAuthBaseURL() { data['authBaseURL'] }
    String getLogin() { data['login'] }
    String getPassword() { data['password'] }
    String getAccessTokenUrl() { data['accessTokenUrl'] }
    String getHhApiUrl() { data['hhApiUrl'] }

    Map getUserInfoExpectedData() { data['userinfoExpectedData'] }
    Map getNewUserInfoExpectedData() { data['newUserinfoExpectedData'] }

    String getNewUserFirstName() { data['newUserFirstName']}
    String getNewUserLastName() { data['newUserLastName']}
}
