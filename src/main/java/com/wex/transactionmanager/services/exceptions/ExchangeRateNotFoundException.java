package com.wex.transactionmanager.services.exceptions;

public class ExchangeRateNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ExchangeRateNotFoundException(String message) {
		super(message);
	}
	
	public ExchangeRateNotFoundException() {
		super("Exchange Rate not found");
	}

}