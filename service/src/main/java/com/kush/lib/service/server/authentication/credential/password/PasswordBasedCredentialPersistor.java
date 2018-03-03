package com.kush.lib.service.server.authentication.credential.password;

import java.util.Iterator;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.persistence.helpers.PersistableObject;
import com.kush.lib.persistence.validation.ValidatingPersistor;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;

public class PasswordBasedCredentialPersistor extends ValidatingPersistor<PersistablePasswordBasedCredential> {

    public PasswordBasedCredentialPersistor(Persistor<PersistablePasswordBasedCredential> delegate) {
        super(delegate);
    }

    @Override
    protected void validate(PersistablePasswordBasedCredential object) throws PersistorOperationFailedException {
        String username = object.get().getUsername();
        Iterator<PersistablePasswordBasedCredential> existingCredentials = fetchAll();
        while (existingCredentials.hasNext()) {
            PersistableObject<PasswordBasedCredential> existingPersistableCred = existingCredentials.next();
            PasswordBasedCredential existingCred = existingPersistableCred.get();
            if (existingCred.getUsername().equals(username)) {
                throw new PersistorOperationFailedException("User with username " + username + " already exitsts");
            }
        }
    }
}
