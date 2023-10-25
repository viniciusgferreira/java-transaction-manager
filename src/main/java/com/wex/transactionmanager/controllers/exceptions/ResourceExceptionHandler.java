package com.wex.transactionmanager.controllers.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wex.transactionmanager.exceptions.StandardError;
import com.wex.transactionmanager.services.exceptions.CurrencyNotFoundException;
import com.wex.transactionmanager.services.exceptions.ExchangeRateNotFoundException;
import com.wex.transactionmanager.services.exceptions.PurchaseTransactionNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ValidationError err = new ValidationError();
	    err.addErrors(ex.getBindingResult().getAllErrors());
	    err.setTimestamp(Instant.now());
	    err.setStatus(status.value());
	    err.setMessage("Input field error");
	    err.setError("Validation error");
	    err.setPath(request.getRequestURI());
	    return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardError> handleInputExceptions(HttpMessageNotReadableException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError();
	    err.setTimestamp(Instant.now());
	    err.setStatus(status.value());
	    err.setMessage(ex.getMostSpecificCause().getMessage());
	    err.setError("InvalidInputFormat");
	    err.setPath(request.getRequestURI());
	    return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(PurchaseTransactionNotFoundException.class)
	public ResponseEntity<StandardError> publisherNotFound(PurchaseTransactionNotFoundException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError();
	    err.setTimestamp(Instant.now());
	    err.setStatus(status.value());
	    err.setError("PurchaseTransactionNotFoundException");
	    err.setMessage(ex.getMessage());
	    err.setPath(request.getRequestURI());
	    return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(CurrencyNotFoundException.class)
	public ResponseEntity<StandardError> currencyNotFound(CurrencyNotFoundException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError();
	    err.setTimestamp(Instant.now());
	    err.setStatus(status.value());
	    err.setError("CurrencyNotFoundException");
	    err.setMessage(ex.getMessage());
	    err.setPath(request.getRequestURI());
	    return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(ExchangeRateNotFoundException.class)
	public ResponseEntity<StandardError> currencyRateNotFound(ExchangeRateNotFoundException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError();
	    err.setTimestamp(Instant.now());
	    err.setStatus(status.value());
	    err.setError("ExchangeRateNotFoundException");
	    err.setMessage(ex.getMessage());
	    err.setPath(request.getRequestURI());
	    return ResponseEntity.status(status).body(err);
	}
	
	
}
