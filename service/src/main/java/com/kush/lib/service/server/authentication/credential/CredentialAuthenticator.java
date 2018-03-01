package com.kush.lib.service.server.authentication.credential;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;

public interface CredentialAuthenticator {

    User getUser(Credential credential);
}
