package com.wex.transactionmanager.utils;

import java.util.UUID;

import com.wex.transactionmanager.dtos.CreatePurchaseTransactionDTO;
import com.wex.transactionmanager.models.PurchaseTransaction;

public class PurchaseTransactionBuilder {
    private CreatePurchaseTransactionDTO dto;


    public PurchaseTransactionBuilder fromDTO(CreatePurchaseTransactionDTO dto) {
    	this.dto = dto;
    	return this;
    }

    public PurchaseTransaction build() {
    	PurchaseTransaction entity = new PurchaseTransaction();
    	entity.setId(UUID.randomUUID());
    	entity.setAmountUSD(this.dto.amountUSD());
    	entity.setDate(this.dto.date());
    	entity.setDescription(this.dto.description());
        return entity;
    }
}
