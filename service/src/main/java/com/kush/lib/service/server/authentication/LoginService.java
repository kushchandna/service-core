package com.kush.lib.service.server.authentication;

import java.util.Arrays;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.utils.id.Identifier;

@Service(name = "Login")
public class LoginService extends BaseService {

    @ServiceMethod(name = "register")
    public User register(Credential credential) throws UserRegistrationFailedException {
        return null;
    }

    @ServiceMethod(name = "login")
    public AuthToken login(Credential credential) throws AuthenticationFailedException {
        PasswordBasedCredential pwdCredential = (PasswordBasedCredential) credential;
        boolean usernameMatched = "testusr".equals(pwdCredential.getUsername());
        boolean passwordMatched = Arrays.equals("testpwd".toCharArray(), pwdCredential.getPassword());
        if (!usernameMatched || !passwordMatched) {
            throw new AuthenticationFailedException("Auth failed");
        }
        User user = new User() {

            @Override
            public Identifier getId() {
                return Identifier.id("testid");
            }
        };
        return new AuthToken(user);
    }

    @AuthenticationRequired
    @ServiceMethod(name = "logout")
    public void logout() {
    }
}
