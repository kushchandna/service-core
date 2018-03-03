package com.kush.lib.service.server.authentication.credential.password;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.authentication.credential.CredentialStore;

public class PasswordBasedCredentialStore implements CredentialStore {

    private final PasswordBasedCredentialPersistor pwdCredPersistor;

    public PasswordBasedCredentialStore(PasswordBasedCredentialPersistor pwdCredPersistor) {
        this.pwdCredPersistor = pwdCredPersistor;
        this.pwdCredPersistor.toString();
    }

    @Override
    public User getUserWithCredential(Credential credential) {
        return null;
    }

    @Override
    public void addCredential(User user, Credential credential) {
    }
}
