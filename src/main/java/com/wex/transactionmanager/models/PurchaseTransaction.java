package com.wex.transactionmanager.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

@Entity
public class PurchaseTransaction {
	@Id
	private UUID id;

	@Column(length = 50, nullable = false)
	private String description;

	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@Column(nullable = false)
	@Digits(fraction = 2, integer = 10)
	@DecimalMin(value = "0.00", inclusive = false)
	private BigDecimal amountUSD;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PurchaseTransaction other = (PurchaseTransaction) obj;
		return Objects.equals(id, other.id);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getAmountUSD() {
		return amountUSD;
	}

	public void setAmountUSD(BigDecimal amountUSD) {
		this.amountUSD = amountUSD;
	}
}
