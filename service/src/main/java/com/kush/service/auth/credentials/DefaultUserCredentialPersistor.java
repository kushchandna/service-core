package com.kush.service.auth.credentials;

import java.util.List;
import java.util.Optional;

import com.kush.lib.persistence.api.DelegatingPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.auth.credentials.password.PasswordBasedCredentialHandler;

public class DefaultUserCredentialPersistor extends DelegatingPersister<UserCredential> implements UserCredentialPersister {

    private final CredentialHandler credentialHandler;

    public DefaultUserCredentialPersistor(Persister<UserCredential> delegate) {
        super(delegate);
        credentialHandler = new PasswordBasedCredentialHandler();
    }

    @Override
    public Optional<User> getUserForCredential(Credential credential) throws PersistorOperationFailedException {
        List<UserCredential> allUserCredentials = fetchAll();
        for (UserCredential userCredential : allUserCredentials) {
            Credential savedCredential = userCredential.getCredential();
            if (credentialHandler.canHandle(savedCredential)) {
                if (credentialHandler.matches(credential, savedCredential)) {
                    return Optional.of(userCredential.getUser());
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void addUserCredential(User user, Credential credential) throws PersistorOperationFailedException {
        List<UserCredential> allUserCredentials = fetchAll();
        for (UserCredential userCredential : allUserCredentials) {
            Credential savedCredential = userCredential.getCredential();
            if (credentialHandler.canHandle(savedCredential)) {
                if (credentialHandler.conflicts(credential, savedCredential)) {
                    throw new PersistorOperationFailedException();
                }
            }
        }
        save(new UserCredential(user, credential));
    }
}
