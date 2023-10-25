package com.wex.transactionmanager.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wex.transactionmanager.dtos.CreatePurchaseTransactionDTO;
import com.wex.transactionmanager.dtos.PurchaseTransactionResponseDTO;
import com.wex.transactionmanager.models.ConvertedAmount;
import com.wex.transactionmanager.models.PurchaseTransaction;
import com.wex.transactionmanager.repositories.PurchaseTransactionRepository;
import com.wex.transactionmanager.utils.PurchaseTransactionResponseDTOBuilder;

@ExtendWith(MockitoExtension.class)
public class PurchaseTransactionServiceTest {
    
    @InjectMocks
    private PurchaseTransactionService underTest;

    @Mock
    private PurchaseTransactionRepository repository;

    @Mock
    private PurchaseTransactionResponseDTOBuilder responseBuilder;

    @Mock
    private CurrencyConverterService currencyConverterService;

    @Test
    public void testSavePurchaseTransaction() {
        UUID id = UUID.randomUUID();
        BigDecimal amountUSD = new BigDecimal("100.00");
        LocalDate date = LocalDate.now();
        String description = "Test Purchase";
        CreatePurchaseTransactionDTO createDTO = new CreatePurchaseTransactionDTO(amountUSD, date, description);

        PurchaseTransaction savedEntity = new PurchaseTransaction();
        savedEntity.setId(id);
        savedEntity.setAmountUSD(amountUSD);
        savedEntity.setDate(date);
        savedEntity.setDescription(description);

        PurchaseTransactionResponseDTO responseDTO = new PurchaseTransactionResponseDTO(id, amountUSD, date, description);

        when(repository.save(Mockito.any(PurchaseTransaction.class))).thenReturn(savedEntity);
        when(responseBuilder.fromEntity(Mockito.any(PurchaseTransaction.class))).thenReturn(responseBuilder);
        when(responseBuilder.build()).thenReturn(responseDTO);

        PurchaseTransactionResponseDTO result = underTest.createPurchaseTransaction(createDTO);

        assertEquals(id, result.getId());
        assertEquals(amountUSD, result.getAmountUSD());
        assertEquals(date, result.getDate());
        assertEquals(description, result.getDescription());
    }

    @Test
    public void testGetPurchaseTransactionById() {
    	UUID id = UUID.randomUUID();
        BigDecimal amountUSD = new BigDecimal("100.00");
        LocalDate date = LocalDate.now();
        String description = "Test Purchase";
        PurchaseTransaction entity = new PurchaseTransaction();
        entity.setId(id);
        entity.setAmountUSD(amountUSD);
        entity.setDate(date);
        entity.setDescription(description);

        PurchaseTransactionResponseDTO responseDTO = new PurchaseTransactionResponseDTO(id, amountUSD, date, description);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(responseBuilder.fromEntity(entity)).thenReturn(responseBuilder);
        when(responseBuilder.build()).thenReturn(responseDTO);

        PurchaseTransactionResponseDTO result = underTest.getPurchaseTransactionById(id);

        assertEquals(id, result.getId());
        assertEquals(entity.getAmountUSD(), result.getAmountUSD());
        assertEquals(entity.getDate(), result.getDate());
    }
    
    @Test
    public void testGetPurchaseTransactionByIdWithCurrency() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal amountUSD = new BigDecimal("100.00");
        LocalDate date = LocalDate.now();
        String description = "Test Purchase";
        String currency = "Brazil-Real";
        BigDecimal exchangeRate = new BigDecimal("2.0");
        LocalDate effectiveDate = LocalDate.parse("2023-09-30");
        

        PurchaseTransaction entity = new PurchaseTransaction();
        entity.setId(id);
        entity.setAmountUSD(amountUSD);
        entity.setDate(date);
        entity.setDescription(description);


        ConvertedAmount convertedAmount = new ConvertedAmount();
        convertedAmount.setCurrency(currency);
        convertedAmount.setEffectiveDate(effectiveDate);
        convertedAmount.setExchangeRate(exchangeRate);
        convertedAmount.setAmount(new BigDecimal("200.00"));
        PurchaseTransactionResponseDTO responseDTO = new PurchaseTransactionResponseDTO(id, amountUSD, date, description,Optional.of(convertedAmount));

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.when(currencyConverterService.convertCurrency(currency, amountUSD, date)).thenReturn(convertedAmount);
        Mockito.when(responseBuilder.fromEntity(entity)).thenReturn(responseBuilder);
        Mockito.when(responseBuilder.withConvertedAmount(any())).thenReturn(responseBuilder);
        Mockito.when(responseBuilder.build()).thenReturn(responseDTO);

        PurchaseTransactionResponseDTO result = underTest.getPurchaseTransactionById(id, currency);

        assertEquals(id, result.getId());
        assertEquals(amountUSD, result.getAmountUSD());
        assertEquals(date, result.getDate());
        assertEquals(currency, result.getConvertedAmount().get().getCurrency());
        assertEquals(effectiveDate, result.getConvertedAmount().get().getEffectiveDate());
        assertEquals(exchangeRate, result.getConvertedAmount().get().getExchangeRate());
        assertEquals(amountUSD, result.getAmountUSD());
    }
}