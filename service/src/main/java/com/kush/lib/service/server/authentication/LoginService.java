package com.kush.lib.service.server.authentication;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.credential.CredentialStore;
import com.kush.utils.id.IdGenerator;
import com.kush.utils.id.Identifier;

@Service(name = "Login")
public class LoginService extends BaseService {

    public static final String KEY_USER_ID_GEN = "USER_ID_GEN";

    @ServiceMethod(name = "register")
    public User register(Credential credential) throws UserRegistrationFailedException {
        CredentialStore credentialStore = getCredentialStore();
        User user = credentialStore.getUserWithCredential(credential);
        if (user != null) {
            throw new UserRegistrationFailedException("User with specified credential already exists");
        }
        user = createUser();
        credentialStore.addCredential(user, credential);
        return user;
    }

    @ServiceMethod(name = "login")
    public AuthToken login(Credential credential) throws AuthenticationFailedException {
        CredentialStore credentialStore = getCredentialStore();
        User user = credentialStore.getUserWithCredential(credential);
        SessionManager sessionManager = getSessionManager();
        return sessionManager.startSession(user);
    }

    @AuthenticationRequired
    @ServiceMethod(name = "logout")
    public void logout() {
        User currentUser = getCurrentUser();
        SessionManager sessionManager = getSessionManager();
        sessionManager.endSession(currentUser);
    }

    private CredentialStore getCredentialStore() {
        return getContext().getInstance(CredentialStore.class);
    }

    private SessionManager getSessionManager() {
        return getContext().getInstance(SessionManager.class);
    }

    private User createUser() {
        IdGenerator idGenerator = getContext().getInstance(KEY_USER_ID_GEN, IdGenerator.class);
        Identifier userId = idGenerator.next();
        return new DefaultUser(userId);
    }
}
