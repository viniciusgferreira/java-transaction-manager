package com.wex.transactionmanager.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wex.transactionmanager.dtos.CreatePurchaseTransactionDTO;
import com.wex.transactionmanager.dtos.PurchaseTransactionResponseDTO;
import com.wex.transactionmanager.services.PurchaseTransactionService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

@RestController()
@RequestMapping(value = "/api/transactions")
public class PurchaseTransactionController {

	@Autowired
	PurchaseTransactionService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<PurchaseTransactionResponseDTO> getTransactionById(@PathVariable UUID id) {
		PurchaseTransactionResponseDTO purchaseTransaction = service.getPurchaseTransactionById(id);
		return ResponseEntity.ok(purchaseTransaction);
	}

	@GetMapping(value = "/{id}", params = "currency")
	public ResponseEntity<PurchaseTransactionResponseDTO> getTransactionByIdWithCurrency(
			@PathVariable 
			UUID id, 
			@RequestParam(value = "currency", required = false) 
			@Parameter(description = "List of Available currencies, visit: https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange" , 
			example = "Brazil-Real")
			String currency) {
		PurchaseTransactionResponseDTO purchaseTransaction = service.getPurchaseTransactionById(id, currency);
		return ResponseEntity.ok(purchaseTransaction);
	}

	@GetMapping
	public ResponseEntity<Page<PurchaseTransactionResponseDTO>> getAllTransactions(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		PageRequest pageable = PageRequest.of(page, size);
		Page<PurchaseTransactionResponseDTO> responsePage = service.getAllTransactions(pageable);
		return ResponseEntity.ok(responsePage);
	}
	
	@PostMapping
	public ResponseEntity<PurchaseTransactionResponseDTO> createTransaction(@Valid @RequestBody CreatePurchaseTransactionDTO dto) {
		PurchaseTransactionResponseDTO purchaseTransaction = service.createPurchaseTransaction(dto);
		URI location = URI.create("/api/transactions/" + purchaseTransaction.getId());
		return ResponseEntity.created(location).body(purchaseTransaction);
	}
}
