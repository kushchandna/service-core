package com.kush.lib.service.server.authentication;

import com.kush.lib.service.remoting.auth.User;

public interface CredentialAuthenticator {

    User getUser(Credential credential);
}
