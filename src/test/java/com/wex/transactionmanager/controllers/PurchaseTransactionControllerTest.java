package com.wex.transactionmanager.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.transactionmanager.dtos.CreatePurchaseTransactionDTO;
import com.wex.transactionmanager.dtos.PurchaseTransactionResponseDTO;
import com.wex.transactionmanager.models.ConvertedAmount;
import com.wex.transactionmanager.services.PurchaseTransactionService;
import com.wex.transactionmanager.services.exceptions.PurchaseTransactionNotFoundException;

@WebMvcTest(PurchaseTransactionController.class)
public class PurchaseTransactionControllerTest {
	private static final String ENDPOINT_PATH = "/api/transactions";

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@MockBean
	private PurchaseTransactionService service;

	@Test
	public void testCreateWithoutArgsShouldReturn400BadRequest() throws Exception {
		BigDecimal amountUSD = null;
		LocalDate date = null;
		String description = null;
		CreatePurchaseTransactionDTO createDTO = new CreatePurchaseTransactionDTO(amountUSD, date, description);

		String requestBody = objectMapper.writeValueAsString(createDTO);

		mockMvc.perform(post(ENDPOINT_PATH).contentType("application/json")
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andDo(print())
		;
		
		verify(service, times(0)).createPurchaseTransaction(createDTO);
	}

	@Test
	public void testCreateShouldReturn201Created() throws Exception {
		BigDecimal amountUSD = new BigDecimal("100.00");
		LocalDate date = LocalDate.now();
		String description = "Test Purchase";
		UUID id = UUID.randomUUID();
		CreatePurchaseTransactionDTO createDTO = new CreatePurchaseTransactionDTO(amountUSD, date, description);
		PurchaseTransactionResponseDTO responseDTO = new PurchaseTransactionResponseDTO(id, amountUSD, date, description);
		when(service.createPurchaseTransaction(createDTO)).thenReturn(responseDTO);
		String requestBody = objectMapper.writeValueAsString(createDTO);

		mockMvc.perform(post(ENDPOINT_PATH).contentType("application/json")
	            .content(requestBody))
	            .andExpect(status().isCreated())
	            .andDo(print());
		
		verify(service, times(1)).createPurchaseTransaction(createDTO);
	}
	
	@Test
	public void testGetWithRandomIdShouldReturn404NotFound() throws Exception {
	    UUID id = UUID.randomUUID();
	    String requestURI = ENDPOINT_PATH + "/" + id;
	 
	    when(service.getPurchaseTransactionById(id)).thenThrow(PurchaseTransactionNotFoundException.class);
	 
	    mockMvc.perform(get(requestURI))
	        .andExpect(status().isNotFound())
	        .andDo(print());
	}
	
	@Test
	public void testGetByIdShouldReturn200OK() throws Exception {
		BigDecimal amountUSD = new BigDecimal("100.00");
		LocalDate date = LocalDate.now();
		String description = "Test Purchase";
		UUID id = UUID.randomUUID();
		PurchaseTransactionResponseDTO responseDTO = new PurchaseTransactionResponseDTO(id, amountUSD, date, description);
	    String requestURI = ENDPOINT_PATH + "/" + id;
	 
	    when(service.getPurchaseTransactionById(id)).thenReturn(responseDTO);
	 
	    mockMvc.perform(get(requestURI))
	        .andExpectAll(
	        		status().isOk(),
	        		jsonPath("$.purchaseTransaction.id", is(id.toString())))
	        .andDo(print());
	    
		verify(service, times(1)).getPurchaseTransactionById(id);

	}
	
	@Test
	public void testGetByIdWithCurrencyShouldReturn200OK() throws Exception {
		BigDecimal amountUSD = new BigDecimal("100.00");
		LocalDate date = LocalDate.now();
		String description = "Test Purchase";
		String currency = "Brazil-Real";
		UUID id = UUID.randomUUID();
		BigDecimal exchangeRate = new BigDecimal("2.0");
		BigDecimal amount = amountUSD.multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN);
        LocalDate effectiveDate = LocalDate.parse("2023-09-30");
        ConvertedAmount convertedAmount = new ConvertedAmount(amount,currency, effectiveDate, exchangeRate);
        System.out.println(convertedAmount.toString());
		PurchaseTransactionResponseDTO responseDTO = new PurchaseTransactionResponseDTO(id, amountUSD, date, description,Optional.of(convertedAmount));
		String requestURI = ENDPOINT_PATH + "/" + id + "?" + "currency=" + currency;
	 
	    when(service.getPurchaseTransactionById(id, currency)).thenReturn(responseDTO);
	 
	    mockMvc.perform(get(requestURI))
	        .andExpectAll(
	        		status().isOk(),
	        		jsonPath("$.purchaseTransaction.id", is(id.toString())))
	        .andExpect(jsonPath("$.purchaseTransaction.convertedAmount.amount", is(200.00)))
	        .andDo(print());
	}
}
