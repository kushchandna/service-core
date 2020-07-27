package com.kush.service.auth.credentials;

import java.util.Optional;

import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;

public interface UserCredentialPersister extends Persister<UserCredential> {

    Optional<User> getUserForCredential(Credential credential) throws PersistorOperationFailedException;

    void addUserCredential(User user, Credential credential) throws PersistorOperationFailedException;
}
