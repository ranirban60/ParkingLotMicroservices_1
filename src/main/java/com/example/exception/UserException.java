package com.example.exception;

public class UserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	String message;
	
	public UserException() {

	}

	public UserException(String message) {
		this.message = message;
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

