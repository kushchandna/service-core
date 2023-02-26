package com.kush.service.auth.credentials.handlers;

import java.util.Arrays;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.credentials.PasswordBasedCredential;
import com.kush.service.auth.credentials.CredentialHandler;

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
                && Arrays.equals(pwdCredToCheck.getPassword(), referencePwdCred.getPassword())) {
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
