package com.ktds.common.exceptions;

public class PolicyViolationException extends RuntimeException {

	private String message;
	private String redirectUri;

	public PolicyViolationException(String message, String redirectUri) {
		this.message = message;
		this.redirectUri = redirectUri;
	}

	public String getMessage() {
		return message;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

}
