package com.kush.lib.service.server.authentication.credential;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;

public interface CredentialStore {

    User getUserWithCredential(Credential credential);

    void addCredential(User user, Credential credential);
}
