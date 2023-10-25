package com.wex.transactionmanager.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.wex.transactionmanager.models.ConvertedAmount;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("purchaseTransaction")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseTransactionResponseDTO {
    private UUID id;
    private BigDecimal amountUSD;
    private LocalDate date;
    private String description;
    private Optional<ConvertedAmount> convertedAmount;

    public PurchaseTransactionResponseDTO(UUID id, BigDecimal amountUSD, LocalDate date, String description, Optional<ConvertedAmount> convertedAmount) {
        this.id = id;
        this.amountUSD = amountUSD;
        this.date = date;
        this.description = description;
        this.convertedAmount = convertedAmount;
    }
    
    public PurchaseTransactionResponseDTO(UUID id, BigDecimal amountUSD, LocalDate date, String description) {
        this.id = id;
        this.amountUSD = amountUSD;
        this.date = date;
        this.description = description;
    }
    
    public PurchaseTransactionResponseDTO() {}
    
    public UUID getId() {
        return id;
    }

    public BigDecimal getAmountUSD() {
        return amountUSD;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Optional<ConvertedAmount> getConvertedAmount() {
        return convertedAmount;
    }
}
