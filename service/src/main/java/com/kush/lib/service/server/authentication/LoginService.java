package com.kush.lib.service.server.authentication;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.credential.UserCredentialPersistor;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.IdGenerator;
import com.kush.utils.id.Identifier;
import com.kush.utils.id.SequentialIdGenerator;

@Service
public class LoginService extends BaseService {

    public static final String KEY_USER_ID_GEN = "USER_ID_GEN";

    @ServiceMethod
    public User register(Credential credential) throws PersistorOperationFailedException, ValidationFailedException {
        UserCredentialPersistor userCredentialPersistor = getUserCredentialPersistor();
        validateCredentialDoesNotExists(credential, userCredentialPersistor);
        User user = createUser();
        userCredentialPersistor.addUserCredential(user, credential);
        return user;
    }

    @ServiceMethod
    public AuthToken login(Credential credential) throws AuthenticationFailedException {
        UserCredentialPersistor userCredentialPersistor = getUserCredentialPersistor();
        User user = getUser(credential, userCredentialPersistor);
        if (user == null) {
            throw new AuthenticationFailedException("No user exists with specified credential");
        }
        SessionManager sessionManager = getSessionManager();
        return sessionManager.startSession(user);
    }

    @AuthenticationRequired
    @ServiceMethod
    public void logout() {
        User currentUser = getCurrentUser();
        SessionManager sessionManager = getSessionManager();
        sessionManager.endSession(currentUser);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(UserCredentialPersistor.class);
        addIfDoesNotExist(KEY_USER_ID_GEN, new SequentialIdGenerator());
        addIfDoesNotExist(Auth.class, new Auth());
        addIfDoesNotExist(SessionManager.class, new SessionManager());
    }

    private UserCredentialPersistor getUserCredentialPersistor() {
        return getContext().getInstance(UserCredentialPersistor.class);
    }

    private SessionManager getSessionManager() {
        return getContext().getInstance(SessionManager.class);
    }

    private User createUser() {
        IdGenerator idGenerator = getContext().getInstance(KEY_USER_ID_GEN, IdGenerator.class);
        Identifier userId = idGenerator.next();
        return new User(userId);
    }

    private void validateCredentialDoesNotExists(Credential credential, UserCredentialPersistor userCredentialPersistor)
            throws ValidationFailedException {
        User user;
        try {
            user = userCredentialPersistor.getUserForCredential(credential);
            if (user != null) {
                throw new ValidationFailedException("User with specified credential already exists");
            }
        } catch (PersistorOperationFailedException e) {
            throw new ValidationFailedException(e.getMessage(), e);
        }
    }

    private User getUser(Credential credential, UserCredentialPersistor userCredentialPersistor) {
        try {
            return userCredentialPersistor.getUserForCredential(credential);
        } catch (PersistorOperationFailedException e) {
            throw new AuthenticationFailedException(e.getMessage(), e);
        }
    }
}
