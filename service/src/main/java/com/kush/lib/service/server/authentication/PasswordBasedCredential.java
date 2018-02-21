package com.kush.lib.service.server.authentication;

import java.util.Arrays;

public class PasswordBasedCredential implements Credential {

    private final String username;
    private final char[] password;

    public PasswordBasedCredential(String username, char[] password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordBasedCredential [username=" + username + ", password=" + Arrays.toString(password) + "]";
    }
}
