package com.kush.lib.service.server.authentication.credential.password;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.lib.service.server.authentication.credential.CredentialHandler;

public class PasswordBasedCredentialHandler implements CredentialHandler {

    @Override
    public boolean canHandle(Credential credential) {
        return credential instanceof PasswordBasedCredential;
    }

    @Override
    public boolean conflicts(Credential credentialToCheck, Credential referenceCredential) {
        checkCanHandle(credentialToCheck);
        checkCanHandle(referenceCredential);
        PasswordBasedCredential pwdCredToCheck = (PasswordBasedCredential) credentialToCheck;
        PasswordBasedCredential referencePwdCred = (PasswordBasedCredential) referenceCredential;
        if (pwdCredToCheck.getUsername().equals(referencePwdCred.getUsername())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean matches(Credential credentialToCheck, Credential referenceCredential) {
        checkCanHandle(credentialToCheck);
        checkCanHandle(referenceCredential);
        PasswordBasedCredential pwdCredToCheck = (PasswordBasedCredential) credentialToCheck;
        PasswordBasedCredential referencePwdCred = (PasswordBasedCredential) referenceCredential;
        if (pwdCredToCheck.getUsername().equals(referencePwdCred.getUsername())
                && pwdCredToCheck.getPassword().equals(referencePwdCred.getPassword())) {
            return true;
        }
        return false;
    }

    private void checkCanHandle(Credential credential) {
        if (!canHandle(credential)) {
            throw new UnsupportedOperationException();
        }
    }
}
