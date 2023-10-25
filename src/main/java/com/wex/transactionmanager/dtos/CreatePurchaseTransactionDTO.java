package com.wex.transactionmanager.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePurchaseTransactionDTO(
		@NotNull(message = "amountUSD is mandatory")
		@Digits(fraction = 2, integer = 10)
		@DecimalMin(value = "0.00", inclusive = false) 
		BigDecimal amountUSD, 
		@NotNull(message = "date is mandatory")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate date, 
		@Size(max = 50) 
		@NotBlank(message = "description is mandatory")
		String description) {}
