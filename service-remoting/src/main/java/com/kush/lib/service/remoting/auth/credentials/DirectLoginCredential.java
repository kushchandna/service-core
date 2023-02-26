package com.kush.lib.service.remoting.auth.credentials;

import com.kush.lib.service.remoting.auth.Credential;

public class DirectLoginCredential implements Credential {

	private static final long serialVersionUID = 1L;
	
	private final String username;
	
	public DirectLoginCredential(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
}
