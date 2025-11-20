package com.nimblix.driverdashboard.driverdashboard.exception;

public class DetailsNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public DetailsNotFoundException(String msg) {
		super(msg);
	}
}
