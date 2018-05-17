package com.kush.service.auth.credentials;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;

public interface CredentialAuthenticator {

    User getUser(Credential credential);
}
