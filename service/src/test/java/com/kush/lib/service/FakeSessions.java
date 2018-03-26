package com.kush.lib.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.rules.ExternalResource;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.lib.service.server.authentication.LoginService;

public class FakeSessions extends ExternalResource {

    private final Map<User, Credential> userVsCredential = new HashMap<>();

    private final String prefix;
    private final int count;

    private LoginService loginService;

    public FakeSessions(String prefix, int count) {
        this.prefix = prefix;
        this.count = count;
    }

    @Override
    protected void before() throws Throwable {
        userVsCredential.clear();
    }

    @Override
    protected void after() {
        try {
            endSession();
        } catch (Exception e) {
            // eat exception
        }
        userVsCredential.clear();
        loginService = null;
    }

    public void initialize(LoginService loginService) throws Exception {
        this.loginService = loginService;
        for (int i = 1; i <= count; i++) {
            Credential credential = new PasswordBasedCredential(prefix + "usr" + i, (prefix + "pwd" + i).toCharArray());
            User user = loginService.register(credential);
            userVsCredential.put(user, credential);
        }
    }

    public User[] getUsers() {
        return userVsCredential.keySet().toArray(new User[userVsCredential.size()]);
    }

    public void beginTestSession() throws Exception {
        beginSession(getUsers()[0]);
    }

    public void beginSession(User user) throws Exception {
        loginService.login(userVsCredential.get(user));
    }

    public void endSession() throws Exception {
        loginService.logout();
    }
}
