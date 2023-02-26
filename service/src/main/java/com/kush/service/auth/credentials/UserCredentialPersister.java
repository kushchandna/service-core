package com.kush.service.auth.credentials;

import java.util.Optional;

import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;

public interface UserCredentialPersister extends Persister<UserCredential> {

    Optional<User> getUserForCredential(Credential credential) throws PersistenceOperationFailedException;

    void addUserCredential(User user, Credential credential) throws PersistenceOperationFailedException;
}
