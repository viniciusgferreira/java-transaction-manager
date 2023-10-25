package com.wex.transactionmanager.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.wex.transactionmanager.models.ConvertedAmount;

@ExtendWith(MockitoExtension.class)
public class CurrencyConverterServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private CurrencyConverterService underTest;

	@Test
	public void testConvertCurrency_Success() {
		String currency = "Brazil-Real";
		BigDecimal amount = new BigDecimal("100.00");
		LocalDate date = LocalDate.now();

		List<String> currencies = Arrays.asList(currency);
		underTest.setCurrencies(currencies);
		when(restTemplate.getForEntity(any(URI.class), eq(String.class)))
		.thenReturn(new ResponseEntity<>("{\"data\":[{\"country_currency_desc\":\"Brazil-Real\", \"exchange_rate\":2.0, \"effective_date\":\"2023-09-30\"}]}", HttpStatus.OK));

		ConvertedAmount convertedAmount = underTest.convertCurrency(currency, amount, date);

		assertEquals(currency, convertedAmount.getCurrency());
		assert (convertedAmount.getEffectiveDate().isAfter(date.minusMonths(6)) || convertedAmount.getEffectiveDate().isEqual(date.minusMonths(6)))
		&& convertedAmount.getEffectiveDate().isBefore(date) || convertedAmount.getEffectiveDate().isEqual(date);
		assertEquals(new BigDecimal("200.00"), convertedAmount.getAmount());
		assertEquals(new BigDecimal("2.0"), convertedAmount.getExchangeRate());
	}
}
