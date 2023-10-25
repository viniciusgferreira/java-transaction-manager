package com.wex.transactionmanager.controllers.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.wex.transactionmanager.exceptions.StandardError;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;

	private Map<String, String> errors = new HashMap<>();
	public Map<String, String> getErrors() {
		return errors;
	}
	
	private void addError(String fieldName, String message) {
		errors.put(fieldName, message);
	}
	
	public void addErrors(List<ObjectError> errors) {
		errors.forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        this.addError(fieldName, errorMessage);
	    });
	}
	
}