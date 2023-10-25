package com.wex.transactionmanager.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.wex.transactionmanager.dtos.PurchaseTransactionResponseDTO;
import com.wex.transactionmanager.models.ConvertedAmount;
import com.wex.transactionmanager.models.PurchaseTransaction;

@Component
public class PurchaseTransactionResponseDTOBuilder {
    private UUID id;
    private BigDecimal amountUSD;
    private LocalDate date;
    private String description;
	private Optional<ConvertedAmount> convertedAmount;

    public PurchaseTransactionResponseDTOBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public PurchaseTransactionResponseDTOBuilder withAmountUSD(BigDecimal amountUSD) {
        this.amountUSD = amountUSD;
        return this;
    }

    public PurchaseTransactionResponseDTOBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public PurchaseTransactionResponseDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public PurchaseTransactionResponseDTOBuilder withConvertedAmount(Optional<ConvertedAmount> convertedAmount) {
        this.convertedAmount = convertedAmount;
        return this;
    }
    
    public PurchaseTransactionResponseDTO build() {
        return new PurchaseTransactionResponseDTO(id, amountUSD, date, description, convertedAmount);
    }

    public PurchaseTransactionResponseDTOBuilder fromEntity(PurchaseTransaction entity) {
        return new PurchaseTransactionResponseDTOBuilder()
            .withId(entity.getId())
            .withAmountUSD(entity.getAmountUSD())
            .withDate(entity.getDate())
            .withDescription(entity.getDescription());
    }
}
