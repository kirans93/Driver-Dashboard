package com.nimblix.driverdashboard.driverdashboard.exception;

//Save/Update/Delete Failure Exception
public class OperationFailedException extends RuntimeException {
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

 public OperationFailedException(String message) {
     super(message);
 }
}