package com.wex.transactionmanager.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.wex.transactionmanager.dtos.CreatePurchaseTransactionDTO;
import com.wex.transactionmanager.dtos.PurchaseTransactionResponseDTO;
import com.wex.transactionmanager.models.ConvertedAmount;
import com.wex.transactionmanager.models.PurchaseTransaction;
import com.wex.transactionmanager.repositories.PurchaseTransactionRepository;
import com.wex.transactionmanager.services.exceptions.PurchaseTransactionNotFoundException;
import com.wex.transactionmanager.utils.PurchaseTransactionBuilder;
import com.wex.transactionmanager.utils.PurchaseTransactionResponseDTOBuilder;

@Service
public class PurchaseTransactionService {

	@Autowired
	PurchaseTransactionRepository repository;

	@Autowired
	PurchaseTransactionResponseDTOBuilder responseBuilder;

	@Autowired
	CurrencyConverterService converterService;

	public PurchaseTransactionResponseDTO createPurchaseTransaction(CreatePurchaseTransactionDTO dto) {
		PurchaseTransaction entity = new PurchaseTransactionBuilder().fromDTO(dto).build();
		PurchaseTransaction savedEntity = repository.save(entity);
		return responseBuilder.fromEntity(savedEntity).build();		
	}

	public PurchaseTransactionResponseDTO getPurchaseTransactionById(UUID id) {
		Optional<PurchaseTransaction> response = repository.findById(id);
		return responseBuilder.fromEntity(response.orElseThrow(() -> new PurchaseTransactionNotFoundException())).build();		
	}

	public PurchaseTransactionResponseDTO getPurchaseTransactionById(UUID id, String currency) {
		Optional<PurchaseTransaction> response = repository.findById(id);
		PurchaseTransaction entity = response.orElseThrow(() -> new PurchaseTransactionNotFoundException());
		ConvertedAmount convertedAmount = converterService.convertCurrency(currency, entity.getAmountUSD(), entity.getDate());
		return responseBuilder.fromEntity(entity).withConvertedAmount(Optional.of(convertedAmount)).build();
	}

	public Page<PurchaseTransactionResponseDTO> getAllTransactions(PageRequest pageable) {
		Page<PurchaseTransaction> purchaseTransactions = repository.findAll(pageable);
		return purchaseTransactions
				.map(purchaseTransaction -> new PurchaseTransactionResponseDTOBuilder().fromEntity(purchaseTransaction).build());
	}
}
