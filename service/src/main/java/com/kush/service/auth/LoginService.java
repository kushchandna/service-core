package com.kush.service.auth;

import java.util.Optional;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.credentials.UserCredentialPersister;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.IdGenerator;
import com.kush.utils.id.Identifier;
import com.kush.utils.id.SequentialIdGenerator;

@Service
public class LoginService extends BaseService {

    public static final String KEY_USER_ID_GEN = "USER_ID_GEN";

    @ServiceMethod
    public User register(Credential credential) throws PersistorOperationFailedException, ValidationFailedException {
        UserCredentialPersister userCredentialPersistor = getUserCredentialPersistor();
        validateCredentialDoesNotExists(credential, userCredentialPersistor);
        User user = createUser();
        userCredentialPersistor.addUserCredential(user, credential);
        return user;
    }

    @ServiceMethod
    public AuthToken login(Credential credential) throws AuthenticationFailedException {
        UserCredentialPersister userCredentialPersistor = getUserCredentialPersistor();
        Optional<User> user = getUser(credential, userCredentialPersistor);
        if (!user.isPresent()) {
            throw new AuthenticationFailedException("No user exists with specified credential");
        }
        SessionManager sessionManager = getSessionManager();
        return sessionManager.startSession(user.get());
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
        checkContextHasValueFor(UserCredentialPersister.class);
        addIfDoesNotExist(KEY_USER_ID_GEN, new SequentialIdGenerator());
        addIfDoesNotExist(Auth.class, new Auth());
        addIfDoesNotExist(SessionManager.class, new SessionManager());
    }

    private UserCredentialPersister getUserCredentialPersistor() {
        return getContext().getInstance(UserCredentialPersister.class);
    }

    private SessionManager getSessionManager() {
        return getContext().getInstance(SessionManager.class);
    }

    private User createUser() {
        IdGenerator idGenerator = getContext().getInstance(KEY_USER_ID_GEN, IdGenerator.class);
        Identifier userId = idGenerator.next();
        return new User(userId);
    }

    private void validateCredentialDoesNotExists(Credential credential, UserCredentialPersister userCredentialPersistor)
            throws ValidationFailedException {
        try {
            Optional<User> user = userCredentialPersistor.getUserForCredential(credential);
            if (user.isPresent()) {
                throw new ValidationFailedException("User with specified credential already exists");
            }
        } catch (PersistorOperationFailedException e) {
            throw new ValidationFailedException(e.getMessage(), e);
        }
    }

    private Optional<User> getUser(Credential credential, UserCredentialPersister userCredentialPersistor) {
        try {
            return userCredentialPersistor.getUserForCredential(credential);
        } catch (PersistorOperationFailedException e) {
            throw new AuthenticationFailedException(e.getMessage(), e);
        }
    }
}
