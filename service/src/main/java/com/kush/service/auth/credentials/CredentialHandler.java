package com.kush.service.auth.credentials;

import com.kush.lib.service.remoting.auth.Credential;

public interface CredentialHandler {

    boolean canHandle(Credential credential);

    boolean conflicts(Credential credentialToCheck, Credential referenceCredential);

    boolean matches(Credential credentialToCheck, Credential referenceCredential);
}
