package com.kush.lib.service.server.authentication.credential;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;

public interface UserCredentialPersistor extends Persistor<UserCredential> {

    User getUserForCredential(Credential credential) throws PersistorOperationFailedException;

    void addUserCredential(User user, Credential credential) throws PersistorOperationFailedException;
}
