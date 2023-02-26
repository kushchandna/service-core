package com.kush.service.auth.credentials.handlers;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.credentials.DirectLoginCredential;
import com.kush.service.auth.credentials.CredentialHandler;

public class DirectLoginCredentialHandler implements CredentialHandler {

	@Override
	public boolean canHandle(Credential credential) {
		return credential instanceof DirectLoginCredential;
	}

    @Override
    public boolean conflicts(Credential credentialToCheck, Credential referenceCredential) {
        checkCanHandle(credentialToCheck);
        checkCanHandle(referenceCredential);
        DirectLoginCredential pwdCredToCheck = (DirectLoginCredential) credentialToCheck;
        DirectLoginCredential referencePwdCred = (DirectLoginCredential) referenceCredential;
        if (pwdCredToCheck.getUsername().equals(referencePwdCred.getUsername())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean matches(Credential credentialToCheck, Credential referenceCredential) {
        checkCanHandle(credentialToCheck);
        checkCanHandle(referenceCredential);
        DirectLoginCredential pwdCredToCheck = (DirectLoginCredential) credentialToCheck;
        DirectLoginCredential referencePwdCred = (DirectLoginCredential) referenceCredential;
        if (pwdCredToCheck.getUsername().equals(referencePwdCred.getUsername())) {
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
