package com.wex.transactionmanager.services.exceptions;

public class PurchaseTransactionNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PurchaseTransactionNotFoundException(String message) {
		super(message);
	}
	
	public PurchaseTransactionNotFoundException() {
		super("PurchaseTransaction not found");
	}

}