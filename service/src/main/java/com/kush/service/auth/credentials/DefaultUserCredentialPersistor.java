package com.kush.service.auth.credentials;

import java.util.List;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.auth.credentials.password.PasswordBasedCredentialHandler;

public class DefaultUserCredentialPersistor extends DelegatingPersistor<UserCredential> implements UserCredentialPersistor {

    private final CredentialHandler credentialHandler;

    public DefaultUserCredentialPersistor(Persistor<UserCredential> delegate) {
        super(delegate);
        credentialHandler = new PasswordBasedCredentialHandler();
    }

    @Override
    public User getUserForCredential(Credential credential) throws PersistorOperationFailedException {
        List<UserCredential> allUserCredentials = fetchAll();
        for (UserCredential userCredential : allUserCredentials) {
            Credential savedCredential = userCredential.getCredential();
            if (credentialHandler.canHandle(savedCredential)) {
                if (credentialHandler.matches(credential, savedCredential)) {
                    return userCredential.getUser();
                }
            }
        }
        return null;
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
