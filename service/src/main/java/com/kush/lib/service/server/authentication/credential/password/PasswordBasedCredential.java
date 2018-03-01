package com.kush.lib.service.server.authentication.credential.password;

import java.util.Arrays;

import com.kush.lib.service.remoting.auth.Credential;

public class PasswordBasedCredential implements Credential {

    private final String username;
    private final char[] password;

    public PasswordBasedCredential(String username, char[] password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "PasswordBasedCredential [username=" + getUsername() + ", password=" + Arrays.toString(getPassword()) + "]";
    }
}
