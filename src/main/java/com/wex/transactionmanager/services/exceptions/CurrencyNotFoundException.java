package com.wex.transactionmanager.services.exceptions;

public class CurrencyNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CurrencyNotFoundException(String message) {
		super(message);
	}
	
	public CurrencyNotFoundException() {
		super("Currency not found");
	}

}