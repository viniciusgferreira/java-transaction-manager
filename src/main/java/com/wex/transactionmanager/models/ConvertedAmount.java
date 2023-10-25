package com.wex.transactionmanager.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Digits;

public class ConvertedAmount {
    private BigDecimal amount;
    private String currency;
    private LocalDate effectiveDate;
    @Digits(integer = 10, fraction = 3)
    private BigDecimal exchangeRate;
    
    
    
	public ConvertedAmount() {}

	public ConvertedAmount(BigDecimal amount, String currency, LocalDate effectiveDate, BigDecimal exchangeRate) {
		this.amount = amount;
		this.currency = currency;
		this.effectiveDate = effectiveDate;
		this.exchangeRate = exchangeRate;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
    
    
}