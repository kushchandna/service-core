package com.kush.lib.service.server.authentication.credential.password;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.authentication.credential.CredentialStore;

public class PasswordBasedCredentialStore implements CredentialStore {

    public PasswordBasedCredentialStore(PasswordBasedCredentialPersistor pwdPersistor) {
    }

    @Override
    public User getUserWithCredential(Credential credential) {
        return null;
    }

    @Override
    public void addCredential(User user, Credential credential) {
    }
}
